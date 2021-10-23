package com.spaceapps.myapplication.app.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.spaceapps.myapplication.app.INITIAL_PAGE
import com.spaceapps.myapplication.app.local.SpaceAppsDatabase
import com.spaceapps.myapplication.app.local.dao.DevicesDao
import com.spaceapps.myapplication.app.local.dao.DevicesRemoteKeysDao
import com.spaceapps.myapplication.app.models.local.DeviceEntity
import com.spaceapps.myapplication.app.models.local.DeviceRemoteKey
import com.spaceapps.myapplication.app.network.calls.ProfileCalls
import com.spaceapps.myapplication.utils.Error
import com.spaceapps.myapplication.utils.Success
import com.spaceapps.myapplication.utils.request

@OptIn(ExperimentalPagingApi::class)
class DevicesRemoteMediator(
    private val calls: ProfileCalls,
    private val db: SpaceAppsDatabase,
    private val dao: DevicesDao,
    private val keysDao: DevicesRemoteKeysDao
) : RemoteMediator<Int, DeviceEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DeviceEntity>
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
            calls.getProfileDevices(
                size = state.config.pageSize,
                page = loadKey
            )
        }
        return when (response) {
            is Success -> {
                db.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        dao.clearAll()
                        keysDao.clearAll()
                    }
                    val devices = response.data.data
                    val page = response.data.page
                    val prevKey = if (page == INITIAL_PAGE) null else page - 1
                    val nextKey = if (response.data.isLast) null else page + 1
                    val keys = devices.map {
                        DeviceRemoteKey(
                            id = it.id,
                            nextKey = nextKey,
                            prevKey = prevKey,
                        )
                    }
                    dao.insertAll(
                        devices.map {
                            DeviceEntity(
                                id = it.id
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

    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, DeviceEntity>): DeviceRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { l ->
            keysDao.remoteKeysByIdAndQuery(l.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, DeviceEntity>): DeviceRemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { l ->
            keysDao.remoteKeysByIdAndQuery(l.id)
        }
    }
}
