package com.spaceapps.myapplication.features.notificationView

sealed class NotificationViewEvent {

    data class ShowSnackBar(val messageId: Int) : NotificationViewEvent()
}
