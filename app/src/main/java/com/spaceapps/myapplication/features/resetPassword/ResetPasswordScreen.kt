package com.spaceapps.myapplication.features.resetPassword

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.SPACING_16
import com.spaceapps.myapplication.utils.autofill
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ResetPasswordScreen(viewModel: ResetPasswordViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val codeVerified by viewModel.codeVerified.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
    }
    val focusManager = LocalFocusManager.current
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is ResetPasswordEvent.CodeVerificationError -> showErrorDialog = true
            }
        }
    }
    if (showErrorDialog) {
        Dialog(onDismissRequest = { /*TODO*/ }) {
            Box {
                Text(text = "Error")
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SPACING_16)
                    .autofill(onFill = viewModel::onPasswordEnter, AutofillType.Password),
                value = password.text,
                onValueChange = viewModel::onPasswordEnter,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                isError = password.errorId != null,
                label = { Text(text = stringResource(id = password.errorId ?: R.string.password)) },
                enabled = codeVerified
            )
            Spacer(modifier = Modifier.height(SPACING_16))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SPACING_16)
                    .autofill(viewModel::onConfirmPasswordEnter, AutofillType.Password),
                value = confirmPassword.text,
                onValueChange = viewModel::onConfirmPasswordEnter,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                    onDone = { keyboardController?.hide() }
                ),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = confirmPassword.errorId != null,
                label = {
                    Text(
                        text = stringResource(
                            id = confirmPassword.errorId ?: R.string.confirm_password
                        )
                    )
                },
                enabled = codeVerified
            )
            Spacer(modifier = Modifier.height(SPACING_16))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SPACING_16),
                onClick = viewModel::onResetPasswordClick,
                contentPadding = PaddingValues(SPACING_16)
            ) {
                Text(text = stringResource(id = R.string.reset_password))
            }
        }
    }
}
