package com.spaceapps.myapplication.features.conversationsList

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.spaceapps.myapplication.ui.SpaceAppsTheme
import com.spaceapps.myapplication.utils.ComposableFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConversationsListFragment : ComposableFragment() {

    private val vm by viewModels<ConversationsListViewModel>()

    @Composable
    override fun Content() = SpaceAppsTheme { ConversationsListScreen() }
}
