package com.spaceapps.myapplication.features.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.app.GeolocationGraph
import com.spaceapps.myapplication.app.Screens
import com.spaceapps.myapplication.core.models.InputWrapper
import com.spaceapps.myapplication.core.repositories.auth.AuthRepository
import com.spaceapps.myapplication.core.repositories.auth.results.LogOutResult
import com.spaceapps.myapplication.core.repositories.auth.results.SignInResult
import com.spaceapps.myapplication.core.repositories.auth.results.SignUpResult
import com.spaceapps.myapplication.core.repositories.auth.results.SocialSignInResult
import com.spaceapps.myapplication.core.utils.AuthDispatcher
import com.spaceapps.myapplication.core.utils.getStateFlow
import com.spaceapps.myapplication.core.utils.isEmail
import com.spaceapps.myapplication.core.utils.isPassword
import com.spaceapps.myapplication.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    fun onEmailEnter(input: String) = viewModelScope.launch {
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

    fun onPasswordEnter(input: String) = viewModelScope.launch {
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

    fun onConfirmPasswordEnter(input: String) = viewModelScope.launch {
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

    fun onAuthClick() = when (isSignUp.value) {
        true -> signUp()
        false -> signIn()
    }

    fun onHaveAccountClick() = viewModelScope.launch { isSignUp.emit(!isSignUp.value) }

    private fun signIn() = viewModelScope.launch {
        when (repository.signIn(email = email.value.text, password = password.value.text)) {
            SignInResult.Success -> goGeolocationScreen()
            SignInResult.Failure -> Timber.e("ERROR")
        }
    }

    private fun signUp() = viewModelScope.launch {
        when (repository.signUp(email = email.value.text, password = password.value.text)) {
            SignUpResult.Success -> goGeolocationScreen()
            SignUpResult.Failure -> Timber.e("ERROR")
        }
    }

    private fun goGeolocationScreen() = navigationDispatcher.emit {
        it.navigate(GeolocationGraph.route) {
            launchSingleTop = true
            popUpTo(Screens.Auth.route) {
                inclusive = true
            }
        }
    }

    private fun signInWithGoogle(accessToken: String) = viewModelScope.launch {
        when (repository.signInWithGoogle(accessToken = accessToken)) {
            SocialSignInResult.Success -> navigationDispatcher.emit {
                it.navigate(
                    GeolocationGraph.route
                )
            }
            SocialSignInResult.Failure -> Timber.e("ERROR")
        }
    }

    private fun signInWithFacebook(accessToken: String) = viewModelScope.launch {
        when (repository.signInWithFacebook(accessToken = accessToken)) {
            SocialSignInResult.Success -> navigationDispatcher.emit {
                it.navigate(
                    GeolocationGraph.route
                )
            }
            SocialSignInResult.Failure -> Timber.e("ERROR")
        }
    }

    private fun signInWithApple(accessToken: String) = viewModelScope.launch {
        when (repository.signInWithApple(accessToken = accessToken)) {
            SocialSignInResult.Success -> navigationDispatcher.emit {
                it.navigate(
                    GeolocationGraph.route
                )
            }
            SocialSignInResult.Failure -> Timber.e("ERROR")
        }
    }

    private fun logOut() = viewModelScope.launch {
        when (repository.logOut()) {
            LogOutResult.Success -> authDispatcher.requestLogOut()
            LogOutResult.Failure -> Timber.e("ERROR")
        }
    }
}
