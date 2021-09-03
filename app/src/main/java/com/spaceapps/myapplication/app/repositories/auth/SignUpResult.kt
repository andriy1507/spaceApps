package com.spaceapps.myapplication.app.repositories.auth

sealed class SignUpResult {
    object Success : SignUpResult()
    object Failure : SignUpResult()
}
