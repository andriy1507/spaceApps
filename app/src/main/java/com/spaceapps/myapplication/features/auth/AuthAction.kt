package com.spaceapps.myapplication.features.auth

sealed class AuthAction {
    object SignInWithSocialClick : AuthAction()
    object AuthButtonClick : AuthAction()
    object ForgotPasswordClick : AuthAction()
    object HaveAccountClick : AuthAction()
}
