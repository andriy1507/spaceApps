package com.spaceapps.myapplication.features.chat

import com.spaceapps.myapplication.models.remote.chat.MessageResponse

sealed class ChatEvent
class MessageReceived(val message: MessageResponse) : ChatEvent()
object Connected : ChatEvent()
object Disconnected : ChatEvent()
