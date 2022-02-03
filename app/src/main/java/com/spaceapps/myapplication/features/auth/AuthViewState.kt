package com.spaceapps.myapplication.features.auth

import android.os.Parcelable
import com.spaceapps.myapplication.core.models.InputWrapper
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthViewState(
    val email: InputWrapper = InputWrapper.Empty,
    val password: InputWrapper = InputWrapper.Empty,
    val confirmPassword: InputWrapper = InputWrapper.Empty,
    val screenState: ScreenState = ScreenState.SignIn
) : Parcelable {

    @Parcelize
    enum class ScreenState : Parcelable {
        SignIn,
        SignUp
    }

    companion object {
        val Empty = AuthViewState()
    }

    val isSignUp: Boolean get() = screenState == ScreenState.SignUp
}
