package com.spaceapps.myapplication.features.chat

import com.spaceapps.myapplication.models.remote.chat.ChatMessage

sealed class ChatEvent
class MessageReceived(val message: ChatMessage) : ChatEvent()
object Connected : ChatEvent()
object Disconnected : ChatEvent()
