package com.spaceapps.myapplication.features.forgotPassword

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.spaceapps.myapplication.utils.ComposableFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : ComposableFragment() {

    private val vm by viewModels<ForgotPasswordViewModel>()

    @Composable
    override fun Content() = ForgotPasswordScreen()
}