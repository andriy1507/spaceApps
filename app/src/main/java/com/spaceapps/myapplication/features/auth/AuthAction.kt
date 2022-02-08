package com.spaceapps.myapplication.features.auth

typealias OnActionSubmit = (AuthAction) -> Unit

sealed class AuthAction {
    object SignInWithSocialClick : AuthAction()
    object AuthButtonClick : AuthAction()
    object ForgotPasswordClick : AuthAction()
    object HaveAccountClick : AuthAction()
    object TogglePasswordHidden : AuthAction()
    object ToggleConfirmPasswordHidden : AuthAction()
    data class EmailEntered(val input: String) : AuthAction()
    data class PasswordEntered(val input: String) : AuthAction()
    data class ConfirmPasswordEntered(val input: String) : AuthAction()
}
