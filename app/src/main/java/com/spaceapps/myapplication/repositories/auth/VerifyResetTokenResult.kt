package com.spaceapps.myapplication.repositories.auth

sealed class VerifyResetTokenResult {
    object Success : VerifyResetTokenResult()
    object Failure : VerifyResetTokenResult()
}
