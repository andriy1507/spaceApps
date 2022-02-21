package com.spaceapps.myapplication.features.forgotPassword

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.core.models.InputWrapper
import com.spaceapps.myapplication.core.repositories.auth.AuthRepository
import com.spaceapps.myapplication.core.repositories.auth.results.SendResetCodeResult
import com.spaceapps.myapplication.core.utils.getStateFlow
import com.spaceapps.myapplication.core.utils.isEmail
import com.spaceapps.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val navigator: Navigator,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _events = MutableSharedFlow<ForgotPasswordScreenEvent>()
    val events get() = _events.asSharedFlow()

    val email = savedStateHandle.getStateFlow(
        scope = viewModelScope,
        key = "email",
        initialValue = InputWrapper()
    )

    fun onEmailEnter(input: String) = viewModelScope.launch {
        val errorId = if (input.isEmail) null else R.string.invalid_email
        email.emit(InputWrapper(text = input, errorId = errorId))
    }

    fun onSendResetCodeClick() = viewModelScope.launch {
        if (!email.value.text.isEmail) return@launch
        when (repository.sendResetCode(email = email.value.text)) {
            SendResetCodeResult.Success -> {
            }
            SendResetCodeResult.Failure ->
                _events.emit(ForgotPasswordScreenEvent.ShowSnackBar(R.string.unexpected_error))
        }
    }
}
