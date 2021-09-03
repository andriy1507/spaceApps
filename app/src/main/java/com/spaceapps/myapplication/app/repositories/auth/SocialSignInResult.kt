package com.spaceapps.myapplication.app.repositories.auth

sealed class SocialSignInResult {
    object Success : SocialSignInResult()
    object Failure : SocialSignInResult()
}
