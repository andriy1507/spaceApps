package com.spaceapps.myapplication.features.auth

import androidx.annotation.StringRes

sealed class AuthScreenEvent
object AuthInitialState : AuthScreenEvent()
data class InputError(
    @StringRes val email: Int? = null,
    @StringRes val password: Int? = null,
    @StringRes val confirmPassword: Int? = null,
) : AuthScreenEvent()

object SignInWithGoogle : AuthScreenEvent()
object SignInWithFacebook : AuthScreenEvent()
object SignInWithApple : AuthScreenEvent()
