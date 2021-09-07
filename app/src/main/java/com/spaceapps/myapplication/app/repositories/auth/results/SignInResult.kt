package com.spaceapps.myapplication.app.repositories.auth.results

sealed class SignInResult {
    object Success : SignInResult()
    object Failure : SignInResult()
}
