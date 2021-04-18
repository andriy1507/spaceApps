package com.spaceapps.myapplication.features.auth

import androidx.lifecycle.SavedStateHandle
import androidx.test.filters.SmallTest
import com.spaceapps.myapplication.CoroutineScopeRule
import com.spaceapps.myapplication.repositories.auth.AuthRepository
import com.spaceapps.myapplication.utils.NavDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@SmallTest
@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineScopeRule()

    private lateinit var viewModel: AuthViewModel

    @Mock
    private lateinit var authRepository: AuthRepository

    @Mock
    private lateinit var navDispatcher: NavDispatcher

    @Before
    fun setUp() {
        viewModel = AuthViewModel(authRepository, navDispatcher, SavedStateHandle())
    }

    @Test
    fun `assert sign in is called with valid email and password`() = runBlockingTest {
        // TODO 4/18/2021 Fix problem with test coroutines
//        viewModel.state.postValue(SignInState)
//        viewModel.onEmailEntered(validEmail)
//        viewModel.onPasswordEntered(validPassword)
//        viewModel.onAuthButtonClick()
//        Mockito.verify(authRepository, Mockito.atLeastOnce()).signIn(validEmail, validPassword)
    }

    companion object {
        private const val validEmail = "email@mail.com"
        private const val validPassword = "string123"
    }
}
