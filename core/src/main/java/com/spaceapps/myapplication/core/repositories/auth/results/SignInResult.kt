package com.spaceapps.myapplication.core.repositories.auth.results

sealed class SignInResult {
    object Success : SignInResult()
    object Failure : SignInResult()
}
