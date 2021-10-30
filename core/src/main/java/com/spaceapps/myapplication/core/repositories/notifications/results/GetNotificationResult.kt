package com.spaceapps.myapplication.core.repositories.notifications.results

import com.spaceapps.myapplication.core.models.remote.notifications.NotificationFullResponse

sealed class GetNotificationResult {
    data class Success(val notification: NotificationFullResponse) : GetNotificationResult()
    data class Error(val exception: Exception) : GetNotificationResult()
}
