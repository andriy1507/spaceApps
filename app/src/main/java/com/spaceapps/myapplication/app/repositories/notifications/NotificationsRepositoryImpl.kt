package com.spaceapps.myapplication.app.repositories.notifications

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.spaceapps.myapplication.app.INITIAL_PAGE
import com.spaceapps.myapplication.app.PAGE_SIZE
import com.spaceapps.myapplication.app.local.SpaceAppsDatabase
import com.spaceapps.myapplication.app.local.dao.NotificationsDao
import com.spaceapps.myapplication.app.local.dao.NotificationsRemoteKeysDao
import com.spaceapps.myapplication.app.network.NotificationsRemoteMediator
import com.spaceapps.myapplication.app.network.calls.NotificationsCalls
import com.spaceapps.myapplication.app.repositories.notifications.results.DeleteNotificationResult
import com.spaceapps.myapplication.app.repositories.notifications.results.GetNotificationResult
import com.spaceapps.myapplication.utils.DispatchersProvider
import com.spaceapps.myapplication.utils.Error
import com.spaceapps.myapplication.utils.Success
import com.spaceapps.myapplication.utils.request
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalPagingApi::class)
class NotificationsRepositoryImpl @Inject constructor(
    private val db: SpaceAppsDatabase,
    private val calls: NotificationsCalls,
    private val dao: NotificationsDao,
    private val keysDao: NotificationsRemoteKeysDao,
    private val dispatchersProvider: DispatchersProvider
) : NotificationsRepository {

    override fun getNotifications() = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        initialKey = INITIAL_PAGE,
        remoteMediator = NotificationsRemoteMediator(
            calls = calls,
            db = db,
            dao = dao,
            keysDao = keysDao
        ),
        pagingSourceFactory = { dao.pagingSource() }
    )

    override suspend fun deleteNotification(id: Int): DeleteNotificationResult =
        withContext(dispatchersProvider.io) {
            return@withContext when (val response = request { calls.deleteNotification(id) }) {
                is Success -> {
                    dao.deleteById(id)
                    DeleteNotificationResult.Success
                }
                is Error -> {
                    Timber.e(response.error)
                    DeleteNotificationResult.Error(response.error)
                }
            }
        }

    override suspend fun getNotificationById(id: Int): GetNotificationResult =
        withContext(dispatchersProvider.io) {
            return@withContext when (val response = request { calls.getNotificationById(id) }) {
                is Success -> GetNotificationResult.Success(response.data)
                is Error -> GetNotificationResult.Error(response.error)
            }
        }
}
