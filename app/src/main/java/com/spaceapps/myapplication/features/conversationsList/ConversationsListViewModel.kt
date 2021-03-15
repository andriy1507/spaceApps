package com.spaceapps.myapplication.features.conversationsList

import androidx.lifecycle.ViewModel
import com.spaceapps.myapplication.network.ChatApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConversationsListViewModel @Inject constructor(
    private val chatApi: ChatApi
) : ViewModel()
