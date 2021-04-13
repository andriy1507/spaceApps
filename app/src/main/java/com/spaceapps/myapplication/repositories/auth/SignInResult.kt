package com.spaceapps.myapplication.repositories.auth

sealed class SignInResult {
    object Success : SignInResult()
    object Failure : SignInResult()
}
