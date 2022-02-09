package com.spaceapps.myapplication.features.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.spaceapps.myapplication.core.models.InputWrapper
import com.spaceapps.myapplication.ui.SpaceAppsTheme
import kotlinx.coroutines.flow.emptyFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class AuthScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun test_auth_screen() {
        composeTestRule.setContent {
            var state by remember { mutableStateOf(AuthViewState.Empty) }
            SpaceAppsTheme {
                AuthScreen(
                    state = state,
                    onActionSubmit = {
                        state = onAuthActionSubmit(state, it)
                    },
                    events = emptyFlow()
                )
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("AuthScreen")
        val nodes = composeTestRule.onAllNodesWithText(text = "Sign In", useUnmergedTree = true)
        nodes.onFirst().assertIsDisplayed()
    }

    private fun onAuthActionSubmit(state: AuthViewState, action: AuthAction): AuthViewState {
        return when (action) {
            is AuthAction.ConfirmPasswordEntered -> state.copy(confirmPassword = InputWrapper(action.input))
            is AuthAction.EmailEntered -> state.copy(email = InputWrapper(action.input))
            is AuthAction.HaveAccountClick -> state.copy(
                screenState = when (state.screenState) {
                    AuthViewState.ScreenState.SignIn -> AuthViewState.ScreenState.SignUp
                    AuthViewState.ScreenState.SignUp -> AuthViewState.ScreenState.SignIn
                }
            )
            is AuthAction.PasswordEntered -> state.copy(password = InputWrapper(action.input))
            is AuthAction.ToggleConfirmPasswordHidden -> state.copy(isConfirmPasswordHidden = !state.isConfirmPasswordHidden)
            is AuthAction.TogglePasswordHidden -> state.copy(isPasswordHidden = !state.isPasswordHidden)
            else -> state
        }
    }
}
