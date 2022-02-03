package com.spaceapps.myapplication.features.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.app.Screens.*
import com.spaceapps.myapplication.core.models.InputWrapper
import com.spaceapps.myapplication.core.repositories.auth.AuthRepository
import com.spaceapps.myapplication.core.repositories.auth.results.SignInResult
import com.spaceapps.myapplication.core.repositories.auth.results.SignUpResult
import com.spaceapps.myapplication.core.utils.getStateFlow
import com.spaceapps.myapplication.core.utils.isEmail
import com.spaceapps.myapplication.core.utils.isPassword
import com.spaceapps.myapplication.utils.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val navigationDispatcher: NavigationDispatcher,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val email = savedStateHandle.getStateFlow(
        scope = viewModelScope,
        key = "email",
        initialValue = InputWrapper()
    )

    private val password = savedStateHandle.getStateFlow(
        scope = viewModelScope,
        key = "password",
        initialValue = InputWrapper()
    )

    private val confirmPassword = savedStateHandle.getStateFlow(
        scope = viewModelScope,
        key = "confirmPassword",
        initialValue = InputWrapper()
    )

    private val screenState = savedStateHandle.getStateFlow(
        scope = viewModelScope,
        key = "isSignUp",
        initialValue = AuthViewState.ScreenState.SignIn
    )

    private val pendingActions = MutableSharedFlow<AuthAction>()

    val state = combine(
        email,
        password,
        confirmPassword,
        screenState
    ) { email, password, confirmPassword, screenState ->
        AuthViewState(
            email = email,
            password = password,
            confirmPassword = confirmPassword,
            screenState = screenState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = AuthViewState.Empty
    )

    init {
        collectActions()
    }

    private fun collectActions() = viewModelScope.launch {
        pendingActions.collect { action ->
            when (action) {
                is AuthAction.AuthButtonClick -> onAuthClick()
                is AuthAction.ForgotPasswordClick -> onForgotPasswordClick()
                is AuthAction.HaveAccountClick -> onHaveAccountClick()
                is AuthAction.SignInWithSocialClick -> onSignInWithSocialClick()
            }
        }
    }

    fun submitAction(action: AuthAction) = viewModelScope.launch {
        pendingActions.emit(action)
    }

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

    private fun onSignInWithSocialClick() =
        navigationDispatcher.emit { it.navigate(SocialAuth.route) }

    private fun onForgotPasswordClick() =
        navigationDispatcher.emit { it.navigate(ForgotPassword.route) }

    private fun onAuthClick() = when (screenState.value) {
        AuthViewState.ScreenState.SignIn -> signIn()
        AuthViewState.ScreenState.SignUp -> signUp()
    }

    private fun onHaveAccountClick() = viewModelScope.launch {
        val newAuthState = when (screenState.value) {
            AuthViewState.ScreenState.SignIn -> AuthViewState.ScreenState.SignUp
            AuthViewState.ScreenState.SignUp -> AuthViewState.ScreenState.SignIn
        }
        screenState.emit(newAuthState)
    }

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
        it.navigate(GeolocationMap.route) {
            launchSingleTop = true
            popUpTo(Auth.route) {
                inclusive = true
            }
        }
    }
}
