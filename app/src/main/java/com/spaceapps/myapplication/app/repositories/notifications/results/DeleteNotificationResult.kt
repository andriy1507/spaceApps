package com.spaceapps.myapplication.app.repositories.notifications.results

sealed class DeleteNotificationResult {
    object Success : DeleteNotificationResult()
    data class Error(val exception: Exception) : DeleteNotificationResult()
}
