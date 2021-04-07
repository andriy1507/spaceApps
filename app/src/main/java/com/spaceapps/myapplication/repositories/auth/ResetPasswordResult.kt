package com.spaceapps.myapplication.repositories.auth

sealed class ResetPasswordResult {
    object Success : ResetPasswordResult()
    object Failure : ResetPasswordResult()
}