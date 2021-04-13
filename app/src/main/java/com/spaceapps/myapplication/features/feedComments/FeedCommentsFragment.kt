package com.spaceapps.myapplication.features.feedComments

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.spaceapps.myapplication.ui.SpaceAppsTheme
import com.spaceapps.myapplication.utils.ComposableFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedCommentsFragment : ComposableFragment() {

    private val vm by viewModels<FeedCommentsViewModel>()

    @Composable
    override fun Content() = SpaceAppsTheme { FeedCommentsScreen(vm) }
}
