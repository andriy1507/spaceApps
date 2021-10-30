package com.spaceapps.myapplication.core.repositories.auth.results

sealed class ResetPasswordResult {
    object Success : ResetPasswordResult()
    object Failure : ResetPasswordResult()
}
