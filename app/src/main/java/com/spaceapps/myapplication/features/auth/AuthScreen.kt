package com.spaceapps.myapplication.features.auth

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.*

@OptIn(ExperimentalAnimationApi::class)
@Suppress("LongMethod")
@Composable
fun AuthScreen(vm: AuthViewModel) = Column(
    modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colors.background)
        .padding(horizontal = SPACING_16.dp),
    verticalArrangement = Arrangement.Center
) {
    val events by vm.events.collectAsState(initial = AuthInitialState)
    val email by vm.email.observeAsState("")
    val password by vm.password.observeAsState("")
    val confirmPassword by vm.confirmPassword.observeAsState("")
    val state by vm.state.observeAsState(SignInState)
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = email,
        onValueChange = vm::onEmailEntered,
        label = { Text(text = stringResource(R.string.email)) },
        singleLine = true,
        isError = events is InputError && (events as InputError).email != null,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        )
    )
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = password,
        onValueChange = vm::onPasswordEntered,
        label = { Text(text = stringResource(R.string.password)) },
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true,
        isError = events is InputError && (events as InputError).password != null,
        keyboardOptions = KeyboardOptions(
            imeAction = if (state == SignInState) ImeAction.Done else ImeAction.Next,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(onDone = { vm.onAuthButtonClick() })
    )
    AnimatedVisibility(visible = state == SignUpState) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = confirmPassword,
            onValueChange = vm::onConfirmPasswordEntered,
            label = { Text(text = stringResource(R.string.confirm_password)) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            isError = events is InputError && (events as InputError).confirmPassword != null,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(onDone = { vm.onAuthButtonClick() })
        )
    }
    OutlinedButton(
        onClick = vm::onAuthButtonClick,
        modifier = Modifier
            .padding(top = SPACING_16.dp)
            .height(SPACING_48.dp)
            .fillMaxWidth()
    ) { Text(text = stringResource(R.string.sign_in)) }
    Text(
        text = "- OR -",
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(vertical = SPACING_16.dp)
    )
    SocialSignInButton(
        onClick = vm::onGoogleSignInClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = googleColor,
            contentColor = Color.White
        ),
        icon = R.drawable.ic_google,
        text = R.string.continue_with_google
    )
    SocialSignInButton(
        onClick = vm::onFacebookSignInClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = facebookColor,
            contentColor = Color.White
        ),
        icon = R.drawable.ic_facebook,
        text = R.string.continue_with_facebook
    )
    SocialSignInButton(
        onClick = vm::onAppleSignInClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = appleColor,
            contentColor = Color.White
        ),
        icon = R.drawable.ic_apple,
        text = R.string.continue_with_apple_id
    )
}

@Composable
fun SocialSignInButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    colors: ButtonColors,
    @DrawableRes icon: Int,
    @StringRes text: Int
) = Button(
    onClick = onClick,
    modifier = modifier
        .padding(vertical = SPACING_8.dp)
        .height(height = SPACING_48.dp)
        .fillMaxWidth(),
    colors = colors,
    content = {
        Icon(
            painter = painterResource(id = icon),
            tint = Color.Unspecified,
            contentDescription = null
        )
        Text(
            text = stringResource(id = text),
            modifier = Modifier.padding(start = SPACING_8.dp)
        )
    }
)
