package com.spaceapps.myapplication.app.repositories.auth

sealed class SendResetTokenResult {
    object Success : SendResetTokenResult()
    object Failure : SendResetTokenResult()
}
