package com.spaceapps.myapplication.repositories.auth

sealed class SocialSignInResult {
    object Success : SocialSignInResult()
    object Failure : SocialSignInResult()
}