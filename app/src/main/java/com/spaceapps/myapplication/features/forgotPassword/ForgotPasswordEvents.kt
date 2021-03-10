package com.spaceapps.myapplication.features.forgotPassword

sealed class ForgotPasswordEvent
object InitEvent : ForgotPasswordEvent()
object PasswordResetSuccess : ForgotPasswordEvent()