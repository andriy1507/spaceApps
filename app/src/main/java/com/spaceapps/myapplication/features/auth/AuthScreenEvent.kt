package com.spaceapps.myapplication.features.auth

import androidx.annotation.StringRes

sealed class AuthScreenEvent
object AuthInitialState : AuthScreenEvent()
data class EmailError(@StringRes val msg: Int? = null) : AuthScreenEvent()
data class PasswordError(@StringRes val msg: Int? = null) : AuthScreenEvent()
data class ConfirmPasswordError(@StringRes val msg: Int? = null) : AuthScreenEvent()
