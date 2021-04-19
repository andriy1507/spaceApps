package com.spaceapps.myapplication.features.auth

import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.features.termsPolicy.LegalType
import com.spaceapps.myapplication.repositories.auth.AuthRepository
import com.spaceapps.myapplication.repositories.auth.SignInResult
import com.spaceapps.myapplication.repositories.auth.SignUpResult
import com.spaceapps.myapplication.repositories.auth.SocialSignInResult
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

    private fun signIn() = launch {
        if (!isEmailValid() && !isPasswordValid()) return@launch
        when (authRepository.signIn(email = email.value!!, password = password.value!!)) {
            SignInResult.Failure -> events.emit(ShowError(R.string.sign_in_failed))
            SignInResult.Success -> goGeolocation()
        }
    }

    private fun goGeolocation() = navDispatcher.emit { navigate(R.id.goGeolocation) }

    private fun signUp() = launch {
        if (!isEmailValid() && !isPasswordValid() && !isConfirmPasswordValid()) return@launch
        when (authRepository.signUp(email = email.value!!, password = password.value!!)) {
            SignUpResult.Failure -> events.emit(ShowError(R.string.sign_up_failed))
            SignUpResult.Success -> goGeolocation()
        }
    }

    fun onAuthButtonClick() = when (state.value) {
        SignInState -> signIn()
        else -> signUp()
    }

    fun onEmailEntered(input: String) = launch {
        isEmailValid()
        email.postValue(input)
    }

    fun onPasswordEntered(input: String) = launch {
        isPasswordValid()
        password.postValue(input)
    }

    fun onConfirmPasswordEntered(input: String) = launch {
        isConfirmPasswordValid()
        confirmPassword.postValue(input)
    }

    fun onGoogleSignInClick() = launch { events.emit(SignInWithGoogle) }

    fun onFacebookSignInClick() = launch { events.emit(SignInWithFacebook) }

    fun onAppleSignInClick() = launch { events.emit(SignInWithApple) }

    fun signInWithGoogle(accessToken: String) = launch {
        when (authRepository.signInWithGoogle(accessToken = accessToken)) {
            SocialSignInResult.Failure -> events.emit(ShowError(R.string.google_sign_in_failed))
            SocialSignInResult.Success -> goGeolocation()
        }
    }

    fun signInWithFacebook(accessToken: String) = launch {
        when (authRepository.signInWithFacebook(accessToken = accessToken)) {
            SocialSignInResult.Failure -> events.emit(ShowError(R.string.facebook_sign_in_failed))
            SocialSignInResult.Success -> goGeolocation()
        }
    }

    fun signInWithApple(accessToken: String) = launch {
        when (authRepository.signInWithApple(accessToken = accessToken)) {
            SocialSignInResult.Failure -> events.emit(ShowError(R.string.apple_sign_in_failed))
            SocialSignInResult.Success -> goGeolocation()
        }
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

    fun toggleState() {
        state.postValue(if (state.value == SignInState) SignUpState else SignInState)
    }

    fun goForgotPassword() = navDispatcher.emit { navigate(R.id.goForgotPassword) }

    fun goTermsOfUse() = navDispatcher.emit {
        navigate(
            R.id.goLegal,
            bundleOf("legalType" to LegalType.TermsOfUse)
        )
    }

    fun goPrivacyPolicy() = navDispatcher.emit {
        navigate(
            R.id.goLegal,
            bundleOf("legalType" to LegalType.PrivacyPolicy)
        )
    }
}
