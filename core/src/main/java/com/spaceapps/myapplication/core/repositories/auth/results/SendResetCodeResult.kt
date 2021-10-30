package com.spaceapps.myapplication.core.repositories.auth.results

sealed class SendResetCodeResult {
    object Success : SendResetCodeResult()
    object Failure : SendResetCodeResult()
}
