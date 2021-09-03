package com.spaceapps.myapplication.app.repositories.auth

sealed class ResetPasswordResult {
    object Success : ResetPasswordResult()
    object Failure : ResetPasswordResult()
}
