package com.spaceapps.myapplication.app.repositories.auth.results

sealed class ResetPasswordResult {
    object Success : ResetPasswordResult()
    object Failure : ResetPasswordResult()
}
