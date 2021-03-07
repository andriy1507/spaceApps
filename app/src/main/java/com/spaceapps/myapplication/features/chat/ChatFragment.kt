package com.spaceapps.myapplication.features.chat

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.spaceapps.myapplication.utils.ComposableFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : ComposableFragment() {

    private val vm by viewModels<ChatViewModel>()

    @Composable
    override fun Content() = ChatScreen()
}
