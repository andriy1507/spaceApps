package com.spaceapps.myapplication.features.auth

import androidx.annotation.StringRes

sealed class AuthScreenEvent {
    data class ShowSnackBar(@StringRes val messageId: Int) : AuthScreenEvent()
}
