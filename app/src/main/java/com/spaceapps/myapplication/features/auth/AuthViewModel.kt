package com.spaceapps.myapplication.features.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.repositories.AuthRepository
import com.spaceapps.myapplication.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val navDispatcher: NavDispatcher,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val events = MutableSharedFlow<AuthScreenEvent>()
    val state = savedStateHandle.getLiveData<AuthScreenState>("state", SignInState)
    val email = savedStateHandle.getLiveData<String>("email")
    val password = savedStateHandle.getLiveData<String>("password")
    val confirmPassword = savedStateHandle.getLiveData<String>("confirmPassword")

    private fun signIn() = async {
        if (!isEmailValid() && !isPasswordValid()) return@async
        request { authRepository.signIn(email = email.value!!, password = password.value!!) }
            .onSuccess { goGeolocation() }
    }

    private fun goGeolocation() = navDispatcher.emit { navigate(R.id.goGeolocation) }

    private fun signUp() = async {
        if (!isEmailValid() && !isPasswordValid() && !isConfirmPasswordValid()) return@async
        request { authRepository.signUp(email = email.value!!, password = password.value!!) }
            .onSuccess { goGeolocation() }
    }

    fun onAuthButtonClick() = when (state.value) {
        SignInState -> signIn()
        else -> signUp()
    }

    fun onEmailEntered(input: String) = async {
        isEmailValid()
        email.postValue(input)
    }

    fun onPasswordEntered(input: String) = async {
        isPasswordValid()
        password.postValue(input)
    }

    fun onConfirmPasswordEntered(input: String) = async {
        isConfirmPasswordValid()
        confirmPassword.postValue(input)
    }

    fun onGoogleSignInClick() = async { events.emit(SignInWithGoogle) }

    fun onFacebookSignInClick() = async { events.emit(SignInWithFacebook) }

    fun onAppleSignInClick() = async { events.emit(SignInWithApple) }

    fun signInWithGoogle(accessToken: String) = async {
        request { authRepository.signInWithGoogle(accessToken = accessToken) }
            .onSuccess { goGeolocation() }
    }

    fun signInWithFacebook(accessToken: String) = async {
        request { authRepository.signInWithFacebook(accessToken = accessToken) }
            .onSuccess { goGeolocation() }
    }

    fun signInWithApple(accessToken: String) = async {
        request { authRepository.signInWithApple(accessToken = accessToken) }
            .onSuccess { goGeolocation() }
    }

    private suspend fun isPasswordValid(): Boolean {
        val isValid = password.value.isPassword
        val message = if (!isValid) R.string.invalid_password else null
        events.emit(InputError(password = message))
        return isValid
    }

    private suspend fun isEmailValid(): Boolean {
        val isValid = email.value.isEmail
        val message = if (!isValid) R.string.invalid_email else null
        events.emit(InputError(email = message))
        return isValid
    }

    private suspend fun isConfirmPasswordValid(): Boolean {
        val isValid = confirmPassword.value.isPassword && confirmPassword.value == password.value
        val message = if (!isValid) R.string.password_does_not_match else null
        events.emit(InputError(confirmPassword = message))
        return isValid
    }
}
