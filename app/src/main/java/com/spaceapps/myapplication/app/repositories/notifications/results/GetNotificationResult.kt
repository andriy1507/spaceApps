package com.spaceapps.myapplication.app.repositories.notifications.results

import com.spaceapps.myapplication.app.models.remote.notifications.NotificationFullResponse

sealed class GetNotificationResult {
    data class Success(val notification: NotificationFullResponse) : GetNotificationResult()
    data class Error(val exception: Exception) : GetNotificationResult()
}
