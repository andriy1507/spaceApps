package com.spaceapps.myapplication.app.repositories.auth.results

sealed class SendResetTokenResult {
    object Success : SendResetTokenResult()
    object Failure : SendResetTokenResult()
}
