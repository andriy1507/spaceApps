package com.spaceapps.myapplication.app.repositories.auth.results

sealed class VerifyResetTokenResult {
    object Success : VerifyResetTokenResult()
    object Failure : VerifyResetTokenResult()
}
