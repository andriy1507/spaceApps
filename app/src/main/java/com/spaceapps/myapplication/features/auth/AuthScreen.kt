package com.spaceapps.myapplication.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spaceapps.myapplication.ui.SPACING_48
import com.spaceapps.myapplication.ui.SpaceAppsTheme

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() = SpaceAppsTheme(darkTheme = true) {
    AuthScreen()
}

@Composable
fun AuthScreen() =
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        val vm = viewModel(AuthViewModel::class.java)
        val events = vm.events.collectAsState(initial = AuthInitialState)
        Spacer(modifier = Modifier.weight(weight = 1f))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = vm.email.observeAsState("").value,
            onValueChange = { vm.email.postValue(it) },
            label = { Text(text = "Email") },
            singleLine = true,
            isError = events.value is EmailError
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = vm.password.observeAsState("").value,
            onValueChange = { vm.password.postValue(it) },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            isError = events.value is PasswordError
        )
        OutlinedButton(
            onClick = vm::onAuthButtonClick,
            modifier = Modifier
                .height(SPACING_48.dp)
                .fillMaxWidth()
        ) { Text(text = "Sign In") }
        Spacer(modifier = Modifier.weight(weight = 1f))
    }
