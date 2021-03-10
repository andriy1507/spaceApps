package com.spaceapps.myapplication.features.forgotPassword

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class ForgotPasswordState : Parcelable

@Parcelize
object EmailState : ForgotPasswordState()

@Parcelize
object TokenState : ForgotPasswordState()

@Parcelize
object PasswordState : ForgotPasswordState()
