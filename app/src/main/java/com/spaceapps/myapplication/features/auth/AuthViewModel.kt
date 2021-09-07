package com.spaceapps.myapplication.features.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.app.GeolocationGraph
import com.spaceapps.myapplication.app.Screens
import com.spaceapps.myapplication.app.models.InputWrapper
import com.spaceapps.myapplication.app.repositories.auth.AuthRepository
import com.spaceapps.myapplication.app.repositories.auth.results.LogOutResult
import com.spaceapps.myapplication.app.repositories.auth.results.SignInResult
import com.spaceapps.myapplication.app.repositories.auth.results.SignUpResult
import com.spaceapps.myapplication.app.repositories.auth.results.SocialSignInResult
import com.spaceapps.myapplication.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val navigationDispatcher: NavigationDispatcher,
    private val authDispatcher: AuthDispatcher,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val email = savedStateHandle.getStateFlow(
        scope = viewModelScope,
        key = "email",
        initialValue = InputWrapper()
    )

    val password = savedStateHandle.getStateFlow(
        scope = viewModelScope,
        key = "password",
        initialValue = InputWrapper()
    )

    val confirmPassword = savedStateHandle.getStateFlow(
        scope = viewModelScope,
        key = "confirmPassword",
        initialValue = InputWrapper()
    )

    val isSignUp = savedStateHandle.getStateFlow(
        scope = viewModelScope,
        key = "isSignUp",
        initialValue = false
    )

    fun onEmailEnter(input: String) = launch {
        email.emit(
            InputWrapper(
                text = input,
                errorId = when (input.isEmail) {
                    true -> null
                    false -> R.string.invalid_email
                }
            )
        )
    }

    fun onPasswordEnter(input: String) = launch {
        password.emit(
            InputWrapper(
                text = input,
                errorId = when (input.isPassword) {
                    true -> null
                    false -> R.string.invalid_password
                }
            )
        )
    }

    fun onConfirmPasswordEnter(input: String) = launch {
        confirmPassword.emit(
            InputWrapper(
                text = input,
                errorId = when {
                    !input.isPassword -> R.string.invalid_password
                    input != password.value.text -> R.string.password_does_not_match
                    else -> null
                }
            )
        )
    }

    fun onSignInWithSocialClick() =
        navigationDispatcher.emit { it.navigate(Screens.SocialAuth.route) }

    fun onForgotPasswordClick() =
        navigationDispatcher.emit { it.navigate(Screens.ForgotPassword.route) }

    fun onAuthClick() {
        when(isSignUp.value) {
            true -> signUp()
            false -> signIn()
        }
    }


    private fun signIn() = launch {
        when (repository.signIn(email = email.value.text, password = password.value.text)) {
            SignInResult.Success -> navigationDispatcher.emit { it.navigate(GeolocationGraph.route) }
            SignInResult.Failure -> Timber.e("ERROR")
        }
    }

    private fun signUp() = launch {
        when (repository.signUp(email = email.value.text, password = password.value.text)) {
            SignUpResult.Success -> navigationDispatcher.emit { it.navigate(GeolocationGraph.route) }
            SignUpResult.Failure -> Timber.e("ERROR")
        }
    }

    private fun signInWithGoogle(accessToken: String) = launch {
        when (repository.signInWithGoogle(accessToken = accessToken)) {
            SocialSignInResult.Success -> navigationDispatcher.emit { it.navigate(GeolocationGraph.route) }
            SocialSignInResult.Failure -> Timber.e("ERROR")
        }
    }

    private fun signInWithFacebook(accessToken: String) = launch {
        when (repository.signInWithFacebook(accessToken = accessToken)) {
            SocialSignInResult.Success -> navigationDispatcher.emit { it.navigate(GeolocationGraph.route) }
            SocialSignInResult.Failure -> Timber.e("ERROR")
        }
    }

    private fun signInWithApple(accessToken: String) = launch {
        when (repository.signInWithApple(accessToken = accessToken)) {
            SocialSignInResult.Success -> navigationDispatcher.emit { it.navigate(GeolocationGraph.route) }
            SocialSignInResult.Failure -> Timber.e("ERROR")
        }
    }

    private fun logOut() = launch {
        when (repository.logOut()) {
            LogOutResult.Success -> authDispatcher.requestLogOut()
            LogOutResult.Failure -> Timber.e("ERROR")
        }
    }
}
