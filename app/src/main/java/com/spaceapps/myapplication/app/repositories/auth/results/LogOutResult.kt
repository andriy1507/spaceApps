package com.spaceapps.myapplication.app.repositories.auth.results

sealed class LogOutResult {
    object Success : LogOutResult()
    object Failure : LogOutResult()
}
