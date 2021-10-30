package com.spaceapps.myapplication.core.repositories.auth.results

sealed class VerifyResetCodeResult {
    object Success : VerifyResetCodeResult()
    object Failure : VerifyResetCodeResult()
}
