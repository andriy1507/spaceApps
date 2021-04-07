package com.spaceapps.myapplication.repositories.auth

sealed class SendResetTokenResult {
    object Success : SendResetTokenResult()
    object Failure : SendResetTokenResult()
}