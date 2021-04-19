package com.spaceapps.myapplication.features.auth

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.*
import dev.chrisbanes.accompanist.insets.navigationBarsPadding
import dev.chrisbanes.accompanist.insets.statusBarsPadding

@OptIn(ExperimentalAnimationApi::class)
@Suppress("LongMethod")
@Composable
fun AuthScreen(vm: AuthViewModel) = Column(
    modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colors.background)
        .padding(horizontal = SPACING_16.dp)
        .statusBarsPadding()
        .navigationBarsPadding()
) {
    val events by vm.events.collectAsState(initial = AuthInitialState)
    val email by vm.email.observeAsState("")
    val password by vm.password.observeAsState("")
    val confirmPassword by vm.confirmPassword.observeAsState("")
    val state by vm.state.observeAsState(SignInState)
    val passwordFocus = remember { FocusRequester() }
    val confirmPasswordFocus = remember { FocusRequester() }
    val inputService = LocalTextInputService.current
    Spacer(modifier = Modifier.weight(1f))
    Text(
        modifier = Modifier
            .align(Alignment.CenterHorizontally),
        text = stringResource(if (state == SignInState) R.string.sign_in else R.string.sign_up),
        style = TextStyle(fontSize = FONT_36.sp, fontWeight = FontWeight.Bold)
    )
    Spacer(modifier = Modifier.weight(1f))
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
        ),
        keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() })
    )
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(passwordFocus),
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
        keyboardActions = KeyboardActions(
            onDone = {
                passwordFocus.freeFocus()
                inputService?.hideSoftwareKeyboard()
                vm.onAuthButtonClick()
            },
            onNext = { confirmPasswordFocus.requestFocus() }
        )
    )
    AnimatedVisibility(visible = state == SignUpState) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(confirmPasswordFocus),
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
            keyboardActions = KeyboardActions(
                onDone = {
                    confirmPasswordFocus.freeFocus()
                    inputService?.hideSoftwareKeyboard()
                    vm.onAuthButtonClick()
                }
            )
        )
    }
    OutlinedButton(
        onClick = vm::onAuthButtonClick,
        modifier = Modifier
            .padding(top = SPACING_16.dp)
            .height(SPACING_48.dp)
            .fillMaxWidth()
    ) { Text(text = stringResource(if (state == SignInState) R.string.sign_in else R.string.sign_up)) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = SPACING_16.dp)
    ) {
        Divider(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            color = MaterialTheme.colors.onBackground
        )
        Text(
            text = stringResource(R.string.OR),
            modifier = Modifier.padding(horizontal = SPACING_16.dp)
        )
        Divider(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            color = MaterialTheme.colors.onBackground
        )
    }
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
    ClickableText(
        text = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
            ) { append(stringResource(R.string.forgot_password)) }
        },
        modifier = Modifier.padding(top = SPACING_16.dp),
        onClick = { vm.goForgotPassword() }
    )
    Spacer(modifier = Modifier.weight(1f))
    val termsOfUse = stringResource(R.string.terms_of_use)
    val dash = stringResource(R.string.vert_dash)
    val privacyPolicy = stringResource(R.string.privacy_policy)
    ClickableText(
        text = buildAnnotatedString {
            append(termsOfUse)
            append(dash)
            append(privacyPolicy)
        },
        modifier = Modifier.align(Alignment.CenterHorizontally)
    ) {
        when {
            it <= termsOfUse.length -> vm.goTermsOfUse()
            it >= (termsOfUse + dash).length -> vm.goPrivacyPolicy()
        }
    }
    Spacer(modifier = Modifier.weight(1f))
    HaveAccountText(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(bottom = SPACING_16.dp),
        state = state,
        onClick = vm::toggleState
    )
}

@Composable
fun HaveAccountText(modifier: Modifier, state: AuthScreenState, onClick: () -> Unit) {
    val style = SpanStyle(color = MaterialTheme.colors.primary, fontWeight = FontWeight.Bold)
    if (state == SignInState) {
        val dontHaveAccount = stringResource(id = R.string.don_t_have_an_account)
        ClickableText(
            modifier = modifier,
            text = buildAnnotatedString {
                withStyle(style = style) {
                    append(dontHaveAccount)
                    append(' ')
                    withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
                        append(stringResource(id = R.string.sign_up))
                    }
                }
            }
        ) { if (it > dontHaveAccount.length) onClick() }
    } else {
        val alreadyHaveAnAccount = stringResource(id = R.string.already_have_an_account)
        ClickableText(
            modifier = modifier,
            text = buildAnnotatedString {
                withStyle(style = style) {
                    append(alreadyHaveAnAccount)
                    append(' ')
                    withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
                        append(stringResource(id = R.string.sign_in))
                    }
                }
            }
        ) { if (it > alreadyHaveAnAccount.length) onClick() }
    }
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
