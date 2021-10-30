package com.spaceapps.myapplication.core.repositories.auth.results

sealed class LogOutResult {
    object Success : LogOutResult()
    object Failure : LogOutResult()
}
