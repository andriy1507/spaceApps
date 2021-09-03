package com.spaceapps.myapplication.app.repositories.auth

sealed class LogOutResult {
    object Success : LogOutResult()
    object Failure : LogOutResult()
}
