package com.spaceapps.myapplication.features.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class AuthScreenState : Parcelable

@Parcelize
object SignInState : AuthScreenState()

@Parcelize
object SignUpState : AuthScreenState()
