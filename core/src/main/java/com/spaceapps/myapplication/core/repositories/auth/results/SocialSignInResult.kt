package com.spaceapps.myapplication.core.repositories.auth.results

sealed class SocialSignInResult {
    object Success : SocialSignInResult()
    object Failure : SocialSignInResult()
}
