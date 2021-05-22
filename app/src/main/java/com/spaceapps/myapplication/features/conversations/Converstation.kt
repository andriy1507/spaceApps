package com.spaceapps.myapplication.features.conversations

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
private const val NAME_LENGTH = 15
private const val LAST_MESSAGE_LENGTH = 25
data class Conversation(
    val imageUrl: String? = null,
    val name: String = LoremIpsum(NAME_LENGTH).values.joinToString(),
    val lastMessage: String = LoremIpsum(LAST_MESSAGE_LENGTH).values.joinToString()
)
