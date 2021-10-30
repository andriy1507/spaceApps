package com.spaceapps.myapplication.core.repositories.locations

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.spaceapps.myapplication.core.INITIAL_PAGE
import com.spaceapps.myapplication.core.PAGE_SIZE
import com.spaceapps.myapplication.core.local.SpaceAppsDatabase
import com.spaceapps.myapplication.core.local.dao.LocationsDao
import com.spaceapps.myapplication.core.local.dao.LocationsRemoteKeysDao
import com.spaceapps.myapplication.core.models.remote.locations.LocationRequest
import com.spaceapps.myapplication.core.network.LocationsRemoteMediator
import com.spaceapps.myapplication.core.network.calls.LocationsCalls
import com.spaceapps.myapplication.core.repositories.locations.results.CreateLocationResult
import com.spaceapps.myapplication.core.repositories.locations.results.DeleteLocationResult
import com.spaceapps.myapplication.core.repositories.locations.results.UpdateLocationResult
import com.spaceapps.myapplication.core.utils.DispatchersProvider
import com.spaceapps.myapplication.core.utils.Success
import com.spaceapps.myapplication.core.utils.Error
import com.spaceapps.myapplication.core.utils.request
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalPagingApi::class)
class LocationsRepositoryImpl @Inject constructor(
    private val calls: LocationsCalls,
    private val db: SpaceAppsDatabase,
    private val dao: LocationsDao,
    private val keysDao: LocationsRemoteKeysDao,
    private val dispatchersProvider: DispatchersProvider
) : LocationsRepository {

    override fun getLocationsByName(name: String?) = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        initialKey = INITIAL_PAGE,
        remoteMediator = LocationsRemoteMediator(
            calls = calls,
            db = db,
            dao = dao,
            keysDao = keysDao,
            query = name
        ),
        pagingSourceFactory = { dao.pagingSource(name) }
    )

    override suspend fun deleteLocation(id: Int): DeleteLocationResult =
        withContext(dispatchersProvider.io) {
            when (val response = request { calls.deleteLocation(id) }) {
                is Success -> {
                    dao.deleteById(id)
                    DeleteLocationResult.Success
                }
                is Error -> DeleteLocationResult.Error(response.error)
            }
        }

    override suspend fun updateLocation(id: Int, request: LocationRequest): UpdateLocationResult =
        withContext(dispatchersProvider.io) {
            when (val response = request { calls.updateLocation(id, request) }) {
                is Success -> UpdateLocationResult.Success(response.data)
                is Error -> UpdateLocationResult.Error(response.error)
            }
        }

    override suspend fun createLocation(request: LocationRequest): CreateLocationResult =
        withContext(dispatchersProvider.io) {
            when (val response = request { calls.postLocation(request) }) {
                is Success -> CreateLocationResult.Success(response.data)
                is Error -> CreateLocationResult.Error(response.error)
            }
        }
}
