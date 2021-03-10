package com.spaceapps.myapplication.features.settings

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.spaceapps.myapplication.utils.ComposableFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : ComposableFragment() {

    private val vm by viewModels<SettingsViewModel>()

    @Composable
    override fun Content() = SettingsScreen(vm)
}
