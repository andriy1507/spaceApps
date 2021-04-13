package com.spaceapps.myapplication.features.notifications

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.spaceapps.myapplication.ui.SpaceAppsTheme
import com.spaceapps.myapplication.utils.ComposableFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : ComposableFragment() {

    private val vm by viewModels<NotificationsViewModel>()

    @Composable
    override fun Content() = SpaceAppsTheme { NotificationsScreen() }
}
