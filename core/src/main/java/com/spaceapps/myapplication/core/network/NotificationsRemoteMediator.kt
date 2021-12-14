package com.spaceapps.myapplication.core.network

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.spaceapps.myapplication.core.INITIAL_PAGE
import com.spaceapps.myapplication.core.local.SpaceAppsDatabase
import com.spaceapps.myapplication.core.local.dao.NotificationsDao
import com.spaceapps.myapplication.core.local.dao.NotificationsRemoteKeysDao
import com.spaceapps.myapplication.core.models.local.notifications.NotificationEntity
import com.spaceapps.myapplication.core.models.local.notifications.NotificationRemoteKey
import com.spaceapps.myapplication.core.network.calls.NotificationsCalls
import com.spaceapps.myapplication.core.utils.Error
import com.spaceapps.myapplication.core.utils.Success
import com.spaceapps.myapplication.core.utils.request

@OptIn(ExperimentalPagingApi::class)
class NotificationsRemoteMediator(
    private val calls: NotificationsCalls,
    private val db: SpaceAppsDatabase,
    private val dao: NotificationsDao,
    private val keysDao: NotificationsRemoteKeysDao
) : RemoteMediator<Int, NotificationEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NotificationEntity>
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
            calls.getNotifications(
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
                    val notifications = response.data.data
                    val page = response.data.page
                    val prevKey = if (page == INITIAL_PAGE) null else page - 1
                    val nextKey = if (response.data.isLast) null else page + 1
                    val keys = notifications.map {
                        NotificationRemoteKey(
                            id = it.id,
                            nextKey = nextKey,
                            prevKey = prevKey
                        )
                    }
                    dao.insertAll(
                        notifications.map {
                            NotificationEntity(
                                id = it.id,
                                title = it.title,
                                text = it.shortText,
                                created = it.created,
                                isViewed = it.viewed
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

    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, NotificationEntity>): NotificationRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { n ->
            keysDao.remoteKeysById(n.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, NotificationEntity>): NotificationRemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { n ->
            keysDao.remoteKeysById(n.id)
        }
    }
}
