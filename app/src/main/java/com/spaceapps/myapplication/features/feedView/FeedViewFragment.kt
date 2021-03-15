package com.spaceapps.myapplication.features.feedView

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.spaceapps.myapplication.utils.ComposableFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedViewFragment : ComposableFragment() {

    private val vm by viewModels<FeedViewViewModel>()

    @Composable
    override fun Content() = FeedViewScreen(vm)
}
