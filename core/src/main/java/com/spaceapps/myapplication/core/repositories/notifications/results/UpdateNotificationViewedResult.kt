package com.spaceapps.myapplication.core.repositories.notifications.results

sealed class UpdateNotificationViewedResult {
    object Success : UpdateNotificationViewedResult()
    data class Error(val exception: Exception) : UpdateNotificationViewedResult()
}
