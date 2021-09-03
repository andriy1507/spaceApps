package com.spaceapps.myapplication.app.repositories.auth

sealed class SignInResult {
    object Success : SignInResult()
    object Failure : SignInResult()
}
