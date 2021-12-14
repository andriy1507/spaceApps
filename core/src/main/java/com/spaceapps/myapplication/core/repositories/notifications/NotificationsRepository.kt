package com.spaceapps.myapplication.core.repositories.notifications

import androidx.paging.Pager
import com.spaceapps.myapplication.core.models.local.notifications.NotificationEntity
import com.spaceapps.myapplication.core.repositories.notifications.results.DeleteNotificationResult
import com.spaceapps.myapplication.core.repositories.notifications.results.GetNotificationResult
import com.spaceapps.myapplication.core.repositories.notifications.results.UpdateNotificationViewedResult

interface NotificationsRepository {
    fun getNotifications(): Pager<Int, NotificationEntity>
    suspend fun deleteNotification(id: Int): DeleteNotificationResult
    suspend fun getNotificationById(id: Int): GetNotificationResult
    suspend fun updateViewedById(id: Int): UpdateNotificationViewedResult
}
