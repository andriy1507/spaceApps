package com.spaceapps.myapplication.features.forgotPassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.repositories.auth.AuthRepository
import com.spaceapps.myapplication.repositories.auth.ResetPasswordResult
import com.spaceapps.myapplication.repositories.auth.SendResetTokenResult
import com.spaceapps.myapplication.repositories.auth.VerifyResetTokenResult
import com.spaceapps.myapplication.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val navDispatcher: NavDispatcher
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

    private fun sendToken() = launch {
        if (isEmailValid())
            when (authRepository.sendResetToken(email = email.value!!)) {
                SendResetTokenResult.Success -> state.postValue(TokenState)
                SendResetTokenResult.Failure -> Unit
            }
    }

    private fun verifyToken() = launch {
        if (isEmailValid() && isTokenValid())
            when (authRepository.verifyResetToken(email = email.value!!, token = token.value!!)) {
                VerifyResetTokenResult.Success -> state.postValue(PasswordState)
                VerifyResetTokenResult.Failure -> Unit
            }
    }

    private fun resetPassword() = launch {
        if (isEmailValid() && isTokenValid() && isConfirmPasswordValid()) {
            val result = authRepository.resetPassword(
                email = email.value!!,
                token = token.value!!,
                password = password.value!!
            )
            when (result) {
                ResetPasswordResult.Failure -> Unit
                ResetPasswordResult.Success -> navDispatcher.emit {
                    popBackStack(
                        R.id.authScreen,
                        false
                    )
                }
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
        return isPasswordValid() && confirmPassword.value == password.value
    }

    fun onContinueClicked(): Any = when (state.value) {
        EmailState -> sendToken()
        TokenState -> verifyToken()
        PasswordState -> resetPassword()
        null -> Unit
    }
}
