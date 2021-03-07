package com.spaceapps.myapplication.features.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.repositories.AuthRepository
import com.spaceapps.myapplication.utils.async
import com.spaceapps.myapplication.utils.isEmail
import com.spaceapps.myapplication.utils.isPassword
import com.spaceapps.myapplication.utils.request
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
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
    }

    private fun signUp() = async {
        if (!isEmailValid() && !isPasswordValid() && !isConfirmPasswordValid()) return@async
        request { authRepository.signUp(email = email.value!!, password = password.value!!) }
    }

    fun onAuthButtonClick() = if (state.value == SignInState) signIn() else signUp()

    private suspend fun isPasswordValid(): Boolean {
        val isValid = password.value.isPassword
        if (!isValid) events.emit(PasswordError())
        return isValid
    }

    private suspend fun isEmailValid(): Boolean {
        val isValid = email.value.isEmail
        if (!isValid) events.emit(EmailError())
        return isValid
    }

    private suspend fun isConfirmPasswordValid(): Boolean {
        val isValid = confirmPassword.value.isPassword && confirmPassword.value == password.value
        if (!isValid) events.emit(ConfirmPasswordError())
        return isValid
    }
}
