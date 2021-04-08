package com.spaceapps.myapplication.repositories.auth

sealed class SignUpResult {
    object Success : SignUpResult()
    object Failure : SignUpResult()
}
