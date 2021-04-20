package com.spaceapps.myapplication.features.forgotPassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.SPACING_16

@Preview
@Composable
fun ForgotPasswordScreen() =
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
            .padding(horizontal = SPACING_16.dp),
        verticalArrangement = Arrangement.Center,
    ) {

        val vm = viewModel<ForgotPasswordViewModel>()
        val email by vm.email.observeAsState("")
        val code by vm.token.observeAsState("")
        val password by vm.password.observeAsState("")
        val confirmPassword by vm.confirmPassword.observeAsState("")
        val state by vm.state.observeAsState(EmailState)
        val event by vm.events.collectAsState(initial = InitEvent)

        val textFieldModifier = Modifier.fillMaxWidth()
        when (state) {
            EmailState -> OutlinedTextField(
                modifier = textFieldModifier,
                value = email,
                onValueChange = vm::onEmailEntered,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            TokenState -> OutlinedTextField(
                modifier = textFieldModifier,
                value = code,
                onValueChange = vm::onTokenEntered,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            PasswordState -> {
                OutlinedTextField(
                    modifier = textFieldModifier,
                    value = password,
                    onValueChange = vm::onPasswordEntered,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                OutlinedTextField(
                    modifier = textFieldModifier,
                    value = confirmPassword,
                    onValueChange = vm::onConfirmPasswordEntered,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
            }
        }
        OutlinedButton(
            onClick = vm::onContinueClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = SPACING_16.dp)
        ) {
            Text(text = stringResource(R.string.Continue))
        }
    }
