package com.spaceapps.myapplication.features.resetPassword

sealed class ResetPasswordEvent {
    object CodeVerificationError : ResetPasswordEvent()
}
