package com.spaceapps.myapplication.core.repositories.notifications

import androidx.paging.Pager
import com.spaceapps.myapplication.core.models.local.NotificationEntity
import com.spaceapps.myapplication.core.repositories.notifications.results.DeleteNotificationResult
import com.spaceapps.myapplication.core.repositories.notifications.results.GetNotificationResult

interface NotificationsRepository {
    fun getNotifications(): Pager<Int, NotificationEntity>
    suspend fun deleteNotification(id: Int): DeleteNotificationResult
    suspend fun getNotificationById(id: Int): GetNotificationResult
}
