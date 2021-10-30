package com.spaceapps.myapplication.core.repositories.auth.results

sealed class SignUpResult {
    object Success : SignUpResult()
    object Failure : SignUpResult()
}
