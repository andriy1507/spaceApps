package com.spaceapps.myapplication.features.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spaceapps.myapplication.app.GeolocationGraph
import com.spaceapps.myapplication.app.repositories.auth.*
import com.spaceapps.myapplication.app.repositories.auth.results.LogOutResult
import com.spaceapps.myapplication.app.repositories.auth.results.SignInResult
import com.spaceapps.myapplication.app.repositories.auth.results.SignUpResult
import com.spaceapps.myapplication.app.repositories.auth.results.SocialSignInResult
import com.spaceapps.myapplication.utils.AuthDispatcher
import com.spaceapps.myapplication.utils.NavigationDispatcher
import com.spaceapps.myapplication.utils.getStateFlow
import com.spaceapps.myapplication.utils.launch
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

    private val email = savedStateHandle.getStateFlow(
        scope = viewModelScope,
        key = "email",
        initialValue = ""
    )

    private val password = savedStateHandle.getStateFlow(
        scope = viewModelScope,
        key = "password",
        initialValue = ""
    )

    private val confirmPassword = savedStateHandle.getStateFlow(
        scope = viewModelScope,
        key = "confirmPassword",
        initialValue = ""
    )

    fun signIn() = launch {
        when (repository.signIn(email = email.value, password = password.value)) {
            SignInResult.Success -> navigationDispatcher.emit { it.navigate(GeolocationGraph.route) }
            SignInResult.Failure -> Timber.e("ERROR")
        }
    }

    fun signUp() = launch {
        when (repository.signUp(email = email.value, password = password.value)) {
            SignUpResult.Success -> navigationDispatcher.emit { it.navigate(GeolocationGraph.route) }
            SignUpResult.Failure -> Timber.e("ERROR")
        }
    }

    fun signInWithGoogle(accessToken: String) = launch {
        when (repository.signInWithGoogle(accessToken = accessToken)) {
            SocialSignInResult.Success -> navigationDispatcher.emit { it.navigate(GeolocationGraph.route) }
            SocialSignInResult.Failure -> Timber.e("ERROR")
        }
    }

    fun signInWithFacebook(accessToken: String) = launch {
        when (repository.signInWithFacebook(accessToken = accessToken)) {
            SocialSignInResult.Success -> navigationDispatcher.emit { it.navigate(GeolocationGraph.route) }
            SocialSignInResult.Failure -> Timber.e("ERROR")
        }
    }

    fun signInWithApple(accessToken: String) = launch {
        when (repository.signInWithApple(accessToken = accessToken)) {
            SocialSignInResult.Success -> navigationDispatcher.emit { it.navigate(GeolocationGraph.route) }
            SocialSignInResult.Failure -> Timber.e("ERROR")
        }
    }

    fun logOut() = launch {
        when (repository.logOut()) {
            LogOutResult.Success -> authDispatcher.requestLogOut()
            LogOutResult.Failure -> Timber.e("ERROR")
        }
    }
}
