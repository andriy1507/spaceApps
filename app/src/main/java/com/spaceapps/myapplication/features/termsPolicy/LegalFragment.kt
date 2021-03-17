package com.spaceapps.myapplication.features.termsPolicy

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.spaceapps.myapplication.utils.ComposableFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LegalFragment : ComposableFragment() {

    private val vm by viewModels<LegalViewModel>()

    @Composable
    override fun Content() = LegalScreen(vm)
}