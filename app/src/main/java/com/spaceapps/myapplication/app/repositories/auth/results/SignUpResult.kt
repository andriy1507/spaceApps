package com.spaceapps.myapplication.app.repositories.auth.results

sealed class SignUpResult {
    object Success : SignUpResult()
    object Failure : SignUpResult()
}
