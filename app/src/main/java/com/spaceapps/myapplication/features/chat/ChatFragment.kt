package com.spaceapps.myapplication.features.chat

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.spaceapps.myapplication.ui.SpaceAppsTheme
import com.spaceapps.myapplication.utils.ComposableFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : ComposableFragment() {

    private val vm by viewModels<ChatViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.toString()
    }

    @Composable
    override fun Content() = SpaceAppsTheme { ChatScreen() }
}
