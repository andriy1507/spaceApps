package com.spaceapps.myapplication.features.auth

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.FONT_35
import com.spaceapps.myapplication.ui.SPACING_16
import com.spaceapps.myapplication.ui.SPACING_64
import com.spaceapps.myapplication.ui.SPACING_8
import com.spaceapps.myapplication.utils.autofill
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AuthScreen(
    state: AuthViewState,
    onActionSubmit: OnActionSubmit,
    events: Flow<AuthScreenEvent>
) {
    val titleId = when (state.screenState) {
        AuthViewState.ScreenState.SignUp -> R.string.sign_up
        AuthViewState.ScreenState.SignIn -> R.string.sign_in
    }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                is AuthScreenEvent.ShowSnackBar ->
                    scaffoldState.snackbarHostState.showSnackbar(context.getString(event.messageId))
            }
        }
    }
    Scaffold(modifier = Modifier.fillMaxSize(), scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AuthScreenHeader(titleId = titleId, onActionSubmit = onActionSubmit)
            AuthScreenInputs(
                state = state,
                onActionSubmit = onActionSubmit,
                focusManager = focusManager,
                keyboardController = keyboardController
            )
            AuthScreenButtons(titleId = titleId, state = state, onActionSubmit = onActionSubmit)
        }
    }
}

@Composable
fun AuthScreenHeader(@StringRes titleId: Int, onActionSubmit: OnActionSubmit) {
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
        onClick = { onActionSubmit(AuthAction.SignInWithSocialClick) }
    ) {
        Text(text = stringResource(id = R.string.sign_in_with_social))
    }
}

@ExperimentalComposeUiApi
@Composable
fun ColumnScope.AuthScreenInputs(
    state: AuthViewState,
    onActionSubmit: OnActionSubmit,
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = SPACING_16, end = SPACING_16, bottom = SPACING_16)
            .autofill(
                onFill = { onActionSubmit(AuthAction.EmailEntered(it)) },
                AutofillType.EmailAddress
            ),
        value = state.email.text,
        onValueChange = { onActionSubmit(AuthAction.EmailEntered(it)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        singleLine = true,
        isError = state.email.errorId != null,
        label = { Text(text = stringResource(id = state.email.errorId ?: R.string.email)) }
    )
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = SPACING_16)
            .autofill(
                onFill = { onActionSubmit(AuthAction.PasswordEntered(it)) },
                AutofillType.Password
            ),
        value = state.password.text,
        onValueChange = { onActionSubmit(AuthAction.PasswordEntered(it)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = when (state.screenState) {
                AuthViewState.ScreenState.SignUp -> ImeAction.Next
                AuthViewState.ScreenState.SignIn -> ImeAction.Done
            }
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) },
            onDone = { keyboardController?.hide() }
        ),
        singleLine = true,
        visualTransformation = when (state.isPasswordHidden) {
            true -> PasswordVisualTransformation()
            false -> VisualTransformation.None
        },
        isError = state.password.errorId != null,
        label = {
            Text(
                text = stringResource(
                    id = state.password.errorId ?: R.string.password
                )
            )
        },
        trailingIcon = {
            IconButton(onClick = { onActionSubmit(AuthAction.TogglePasswordHidden) }) {
                Icon(
                    imageVector = when (state.isPasswordHidden) {
                        true -> Icons.Filled.Visibility
                        false -> Icons.Filled.VisibilityOff
                    },
                    contentDescription = null
                )
            }
        }
    )
    AnimatedVisibility(visible = state.isSignUp) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = SPACING_16, end = SPACING_16, top = SPACING_16)
                .autofill(
                    onFill = { onActionSubmit(AuthAction.ConfirmPasswordEntered(it)) },
                    AutofillType.NewPassword
                ),
            value = state.confirmPassword.text,
            onValueChange = { onActionSubmit(AuthAction.ConfirmPasswordEntered(it)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            singleLine = true,
            visualTransformation = when (state.isConfirmPasswordHidden) {
                true -> PasswordVisualTransformation()
                false -> VisualTransformation.None
            },
            isError = state.confirmPassword.errorId != null,
            label = {
                Text(
                    text = stringResource(
                        id = state.confirmPassword.errorId ?: R.string.confirm_password
                    )
                )
            },
            trailingIcon = {
                IconButton(onClick = { onActionSubmit(AuthAction.ToggleConfirmPasswordHidden) }) {
                    Icon(
                        imageVector = when (state.isConfirmPasswordHidden) {
                            true -> Icons.Filled.Visibility
                            false -> Icons.Filled.VisibilityOff
                        },
                        contentDescription = null
                    )
                }
            }
        )
    }
}

@Composable
fun ColumnScope.AuthScreenButtons(
    @StringRes titleId: Int,
    state: AuthViewState,
    onActionSubmit: OnActionSubmit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = SPACING_16, end = SPACING_16, top = SPACING_16),
        onClick = { onActionSubmit(AuthAction.AuthButtonClick) },
        contentPadding = PaddingValues(SPACING_16)
    ) {
        Text(text = stringResource(id = titleId))
    }
    TextButton(
        modifier = Modifier.padding(top = SPACING_16),
        onClick = { onActionSubmit(AuthAction.ForgotPasswordClick) }
    ) {
        Text(text = stringResource(id = R.string.forgot_password))
    }
    Spacer(modifier = Modifier.weight(1f))
    val haveAccountTextId = when (state.screenState) {
        AuthViewState.ScreenState.SignUp -> R.string.already_have_an_account
        AuthViewState.ScreenState.SignIn -> R.string.don_t_have_an_account
    }
    TextButton(onClick = { onActionSubmit(AuthAction.HaveAccountClick) }) {
        Text(text = stringResource(id = haveAccountTextId))
    }
}
