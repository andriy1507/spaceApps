package com.spaceapps.myapplication.features.feedsList

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.spaceapps.myapplication.utils.ComposableFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedsListFragment : ComposableFragment() {

    private val vm by viewModels<FeedsListViewModel>()

    @Composable
    override fun Content() = FeedsListScreen()
}
