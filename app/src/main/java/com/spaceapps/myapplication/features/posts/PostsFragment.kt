package com.spaceapps.myapplication.features.posts

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.spaceapps.myapplication.utils.ComposableFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostsFragment : ComposableFragment() {

    private val vm by viewModels<PostsViewModel>()

    @Composable
    override fun Content() {
        PostsScreen()
    }
}