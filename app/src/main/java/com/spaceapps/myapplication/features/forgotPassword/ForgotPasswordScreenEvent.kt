package com.spaceapps.myapplication.features.forgotPassword

import androidx.annotation.StringRes

sealed class ForgotPasswordScreenEvent {
    data class ShowSnackBar(@StringRes val messageId: Int) : ForgotPasswordScreenEvent()
}
