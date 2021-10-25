package com.spaceapps.myapplication.app.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.spaceapps.myapplication.app.INITIAL_PAGE
import com.spaceapps.myapplication.app.local.SpaceAppsDatabase
import com.spaceapps.myapplication.app.local.dao.LocationsDao
import com.spaceapps.myapplication.app.local.dao.LocationsRemoteKeysDao
import com.spaceapps.myapplication.app.models.local.LocationEntity
import com.spaceapps.myapplication.app.models.local.LocationRemoteKey
import com.spaceapps.myapplication.app.network.calls.LocationsCalls
import com.spaceapps.myapplication.utils.Error
import com.spaceapps.myapplication.utils.Success
import com.spaceapps.myapplication.utils.request

@OptIn(ExperimentalPagingApi::class)
class LocationsRemoteMediator(
    private val query: String?,
    private val calls: LocationsCalls,
    private val db: SpaceAppsDatabase,
    private val dao: LocationsDao,
    private val keysDao: LocationsRemoteKeysDao
) : RemoteMediator<Int, LocationEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LocationEntity>
    ): MediatorResult {
        val loadKey = when (loadType) {
            LoadType.REFRESH -> INITIAL_PAGE
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state = state)
                when (val nextKey = remoteKeys?.prevKey) {
                    null -> return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    else -> nextKey
                }
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeysForLastItem(state = state)
                when (val nextKey = remoteKeys?.nextKey) {
                    null -> return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    else -> nextKey
                }
            }
        }
        val response = request {
            calls.getLocations(
                size = state.config.pageSize,
                page = loadKey,
                search = query
            )
        }
        return when (response) {
            is Success -> {
                db.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        dao.clearAll()
                        keysDao.clearAll()
                    }
                    val locations = response.data.data
                    val page = response.data.page
                    val prevKey = if (page == INITIAL_PAGE) null else page - 1
                    val nextKey = if (response.data.isLast) null else page + 1
                    val keys = locations.map {
                        LocationRemoteKey(
                            id = it.id,
                            nextKey = nextKey,
                            prevKey = prevKey,
                            query = query
                        )
                    }
                    dao.insertAll(
                        locations.map {
                            LocationEntity(
                                id = it.id,
                                name = it.name,
                                longitude = it.longitude,
                                latitude = it.latitude,
                                altitude = it.altitude,
                                created = it.created
                            )
                        }
                    )
                    keysDao.insertAll(keys)
                }
                MediatorResult.Success(endOfPaginationReached = response.data.isLast)
            }
            is Error -> MediatorResult.Error(response.error)
        }
    }

    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, LocationEntity>): LocationRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { l ->
            keysDao.remoteKeysByIdAndQuery(l.id, query)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, LocationEntity>): LocationRemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { l ->
            keysDao.remoteKeysByIdAndQuery(l.id, query)
        }
    }
}
