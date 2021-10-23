package com.spaceapps.myapplication.app.repositories.devices

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.spaceapps.myapplication.app.INITIAL_PAGE
import com.spaceapps.myapplication.app.PAGE_SIZE
import com.spaceapps.myapplication.app.local.SpaceAppsDatabase
import com.spaceapps.myapplication.app.local.dao.DevicesDao
import com.spaceapps.myapplication.app.local.dao.DevicesRemoteKeysDao
import com.spaceapps.myapplication.app.network.DevicesRemoteMediator
import com.spaceapps.myapplication.app.network.calls.ProfileCalls
import com.spaceapps.myapplication.utils.DispatchersProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalPagingApi::class)
class DevicesRepositoryImpl @Inject constructor(
    private val db: SpaceAppsDatabase,
    private val calls: ProfileCalls,
    private val dao: DevicesDao,
    private val keysDao: DevicesRemoteKeysDao,
    private val dispatchersProvider: DispatchersProvider
) : DevicesRepository {

    override fun getDevices() = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        initialKey = INITIAL_PAGE,
        remoteMediator = DevicesRemoteMediator(
            calls = calls,
            db = db,
            dao = dao,
            keysDao = keysDao
        ),
        pagingSourceFactory = { dao.pagingSource() }
    )
}
