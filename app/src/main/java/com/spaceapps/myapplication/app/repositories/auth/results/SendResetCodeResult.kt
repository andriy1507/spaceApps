package com.spaceapps.myapplication.app.repositories.auth.results

sealed class SendResetCodeResult {
    object Success : SendResetCodeResult()
    object Failure : SendResetCodeResult()
}
