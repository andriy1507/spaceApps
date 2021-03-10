package com.spaceapps.myapplication.features.forgotPassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.repositories.AuthRepository
import com.spaceapps.myapplication.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val email = MutableLiveData<String>()
    val token = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()
    val state = MutableLiveData<ForgotPasswordState>(EmailState)
    val events = MutableSharedFlow<ForgotPasswordEvent>()

    fun onEmailEntered(input: String) {
        isEmailValid()
        email.postValue(input)
    }

    fun onTokenEntered(input: String) {
        isTokenValid()
        token.postValue(input)
    }

    fun onPasswordEntered(input: String) {
        isPasswordValid()
        password.postValue(input)
    }

    fun onConfirmPasswordEntered(input: String) {
        isConfirmPasswordValid()
        confirmPassword.postValue(input)
    }

    private fun sendToken() = async {
        if (isEmailValid()) request { authRepository.sendResetToken(email = email.value!!) }
            .onSuccess { state.postValue(TokenState) }
    }

    private fun verifyToken() = async {
        if (isEmailValid() && isTokenValid()) request {
            authRepository.verifyResetToken(email = email.value!!, token = token.value!!)
        }.onSuccess { state.postValue(PasswordState) }
    }

    private fun resetPassword() = async {
        if (isEmailValid() && isTokenValid() && isPasswordValid() && isConfirmPasswordValid()) {
            request {
                authRepository.resetPassword(
                    email = email.value!!,
                    token = token.value!!,
                    password = password.value!!
                )
            }
        }
    }

    private fun isEmailValid(): Boolean {
        return email.value.isEmail
    }

    private fun isTokenValid(): Boolean {
        return token.value.isResetToken
    }

    private fun isPasswordValid(): Boolean {
        return password.value.isPassword
    }

    private fun isConfirmPasswordValid(): Boolean {
        return confirmPassword.value.isPassword && confirmPassword.value == password.value
    }

    fun onContinueClicked(): Any = when (state.value) {
        EmailState -> sendToken()
        TokenState -> verifyToken()
        PasswordState -> resetPassword()
        null -> Unit
    }
}
