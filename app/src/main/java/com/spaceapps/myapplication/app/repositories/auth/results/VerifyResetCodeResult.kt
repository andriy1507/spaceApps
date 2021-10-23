package com.spaceapps.myapplication.app.repositories.auth.results

sealed class VerifyResetCodeResult {
    object Success : VerifyResetCodeResult()
    object Failure : VerifyResetCodeResult()
}
