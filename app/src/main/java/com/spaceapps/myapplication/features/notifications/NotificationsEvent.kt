package com.spaceapps.myapplication.features.notifications

import androidx.annotation.StringRes

sealed class NotificationsEvent {

    data class ShowSnackBar(@StringRes val messageId: Int) : NotificationsEvent()
}
