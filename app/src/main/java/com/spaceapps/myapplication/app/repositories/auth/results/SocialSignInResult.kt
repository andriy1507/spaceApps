package com.spaceapps.myapplication.app.repositories.auth.results

sealed class SocialSignInResult {
    object Success : SocialSignInResult()
    object Failure : SocialSignInResult()
}
