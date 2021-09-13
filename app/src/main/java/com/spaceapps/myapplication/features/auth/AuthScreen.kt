package com.spaceapps.myapplication.features.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.FONT_35
import com.spaceapps.myapplication.ui.SPACING_16
import com.spaceapps.myapplication.ui.SPACING_64
import com.spaceapps.myapplication.ui.SPACING_8

@Suppress("LongMethod")
@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun AuthScreen(vm: AuthViewModel) {
    val email by vm.email.collectAsState()
    val password by vm.password.collectAsState()
    val confirmPassword by vm.confirmPassword.collectAsState()
    val isSignUp by vm.isSignUp.collectAsState()
    val titleId = when (isSignUp) {
        true -> R.string.sign_up
        false -> R.string.sign_in
    }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(top = SPACING_64),
                text = stringResource(id = titleId),
                style = MaterialTheme.typography.h6,
                fontSize = FONT_35
            )
            Text(
                modifier = Modifier.padding(vertical = SPACING_8),
                text = stringResource(id = R.string.OR),
                style = MaterialTheme.typography.subtitle2
            )
            TextButton(
                modifier = Modifier.padding(bottom = SPACING_64),
                onClick = vm::onSignInWithSocialClick
            ) {
                Text(text = stringResource(id = R.string.sign_in_with_social))
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SPACING_16),
                value = email.text,
                onValueChange = vm::onEmailEnter,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                isError = email.errorId != null,
                label = { Text(text = stringResource(id = email.errorId ?: R.string.email)) }
            )
            Spacer(modifier = Modifier.height(SPACING_16))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SPACING_16),
                value = password.text,
                onValueChange = vm::onPasswordEnter,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = when (isSignUp) {
                        true -> ImeAction.Next
                        false -> ImeAction.Done
                    }
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                    onDone = { keyboardController?.hide() }
                ),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = password.errorId != null,
                label = { Text(text = stringResource(id = password.errorId ?: R.string.password)) }
            )
            if (isSignUp) Spacer(modifier = Modifier.height(SPACING_16))
            AnimatedVisibility(visible = isSignUp) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = SPACING_16),
                    value = confirmPassword.text,
                    onValueChange = vm::onConfirmPasswordEnter,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
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
                    }
                )
            }
            Spacer(modifier = Modifier.height(SPACING_16))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SPACING_16),
                onClick = vm::onAuthClick,
                contentPadding = PaddingValues(SPACING_16)
            ) {
                Text(text = stringResource(id = titleId))
            }
            Spacer(modifier = Modifier.height(SPACING_16))
            TextButton(onClick = vm::onForgotPasswordClick) {
                Text(text = stringResource(id = R.string.forgot_password))
            }
            Spacer(modifier = Modifier.weight(1f))
            val haveAccountTextId = when (isSignUp) {
                true -> R.string.already_have_an_account
                false -> R.string.don_t_have_an_account
            }
            TextButton(onClick = vm::onHaveAccountClick) {
                Text(text = stringResource(id = haveAccountTextId))
            }
        }
    }
}
