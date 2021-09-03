package com.spaceapps.myapplication.app.repositories.auth

sealed class VerifyResetTokenResult {
    object Success : VerifyResetTokenResult()
    object Failure : VerifyResetTokenResult()
}
