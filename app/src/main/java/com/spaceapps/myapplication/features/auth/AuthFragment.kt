package com.spaceapps.myapplication.features.auth

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.spaceapps.myapplication.utils.ComposableFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : ComposableFragment() {

    private val vm by viewModels<AuthViewModel>()

    @Composable
    override fun Content() = AuthScreen()
}
