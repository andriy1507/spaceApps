package com.spaceapps.myapplication.features.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.spaceapps.myapplication.R

@OptIn(ExperimentalAnimationApi::class)
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
    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = stringResource(id = titleId))
            TextButton(onClick = vm::onSignInWithSocialClick) {
                Text(text = stringResource(id = R.string.sign_in_with_social))
            }
            OutlinedTextField(
                value = email.text,
                onValueChange = vm::onEmailEnter
            )
            OutlinedTextField(
                value = password.text,
                onValueChange = vm::onPasswordEnter
            )
            AnimatedVisibility(visible = isSignUp) {
                OutlinedTextField(
                    value = confirmPassword.text,
                    onValueChange = vm::onConfirmPasswordEnter
                )
            }
            Button(onClick = vm::onAuthClick) {
                Text(text = stringResource(id = titleId))
            }
            TextButton(onClick = vm::onForgotPasswordClick) {
                Text(text = stringResource(id = R.string.forgot_password))
            }
            TextButton(onClick = { /*TODO*/ }) {
            }
        }
    }
}
