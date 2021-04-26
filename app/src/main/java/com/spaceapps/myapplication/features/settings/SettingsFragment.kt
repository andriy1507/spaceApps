package com.spaceapps.myapplication.features.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.fragment.app.viewModels
import com.spaceapps.myapplication.Settings.Language.UNRECOGNIZED
import com.spaceapps.myapplication.utils.ComposableFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : ComposableFragment() {

    private val vm by viewModels<SettingsViewModel>()

    @Composable
    override fun Content() {
        val language by vm.language.observeAsState(initial = UNRECOGNIZED)
        SettingsScreen(
            onLogOutClick = vm::logOut,
            onChangeLanguage = vm::setLanguage,
            language = language
        )
    }
}
