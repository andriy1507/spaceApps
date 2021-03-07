package com.spaceapps.myapplication.features.auth

sealed class AuthScreenState
object SignInState : AuthScreenState()
object SignUpState : AuthScreenState()
