package com.spaceapps.myapplication.features.resetPassword

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.core.models.InputWrapper
import com.spaceapps.myapplication.core.repositories.auth.AuthRepository
import com.spaceapps.myapplication.core.repositories.auth.results.ResetPasswordResult
import com.spaceapps.myapplication.core.repositories.auth.results.VerifyResetCodeResult
import com.spaceapps.myapplication.core.utils.getStateFlow
import com.spaceapps.myapplication.core.utils.isPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val repository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val email = savedStateHandle.get<String>("email")
    private val code = savedStateHandle.get<String>("code")
    private val _events = MutableSharedFlow<ResetPasswordEvent>()
    val events: SharedFlow<ResetPasswordEvent>
        get() = _events.asSharedFlow()
    val codeVerified = savedStateHandle.getStateFlow(
        scope = viewModelScope,
        key = "codeVerified",
        initialValue = false
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

    init {
        verifyResetCode()
    }

    private fun verifyResetCode() = viewModelScope.launch {
        when (repository.verifyResetCode(email = email.orEmpty(), code = code.orEmpty())) {
            is VerifyResetCodeResult.Success -> codeVerified.emit(true)
//            is VerifyResetCodeResult.Success -> _events.emit(ResetPasswordEvent.CodeVerificationError)
            is VerifyResetCodeResult.Failure -> _events.emit(ResetPasswordEvent.CodeVerificationError)
        }
    }

    fun onPasswordEnter(input: String) = viewModelScope.launch {
        val error = if (input.isPassword) null else R.string.invalid_password
        password.emit(password.value.copy(text = input, errorId = error))
    }

    fun onConfirmPasswordEnter(input: String) = viewModelScope.launch {
        val error = when {
            input.isPassword && input == password.value.text -> null
            input != password.value.text -> R.string.password_does_not_match
            else -> R.string.invalid_password
        }
        confirmPassword.emit(confirmPassword.value.copy(text = input, errorId = error))
    }

    fun onResetPasswordClick() = viewModelScope.launch {
        if (!codeVerified.value ||
            password.value.errorId != null ||
            confirmPassword.value.errorId != null
        ) return@launch
        val result = repository.resetPassword(
            email = email.orEmpty(),
            code = code.orEmpty(),
            password = password.value.text
        )
        when (result) {
            ResetPasswordResult.Success -> {}
            ResetPasswordResult.Failure -> {}
        }
    }
}
