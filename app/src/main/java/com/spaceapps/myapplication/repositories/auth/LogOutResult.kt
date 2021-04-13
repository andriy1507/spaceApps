package com.spaceapps.myapplication.repositories.auth

sealed class LogOutResult {
    object Success : LogOutResult()
    object Failure : LogOutResult()
}
