package com.spaceapps.myapplication.features.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.models.ChatMessage
import com.spaceapps.myapplication.ui.SPACING_16
import com.spaceapps.myapplication.ui.SPACING_8
import com.spaceapps.myapplication.ui.SpaceAppsTheme
import dev.chrisbanes.accompanist.insets.LocalWindowInsets
import dev.chrisbanes.accompanist.insets.imePadding
import dev.chrisbanes.accompanist.insets.navigationBarsPadding
import dev.chrisbanes.accompanist.insets.toPaddingValues

typealias OnChatMessageEntered = (String) -> Unit
typealias OnSendButtonClicked = () -> Unit

@Preview
@Composable
fun ChatScreenPreview() = SpaceAppsTheme { ChatScreen() }

@Composable
fun ChatScreen() {
    Scaffold(
        topBar = { ChatToolbar() }
    ) {
        Column {
            LazyColumn(
                modifier = Modifier
                    .weight(weight = 1f)
                    .fillMaxWidth()
            ) {
                items(emptyList<ChatMessage>()) { ChatMessageItem() }
            }
            ChatBottomBar()
        }
    }
}

@Composable
fun ChatToolbar() = TopAppBar(
    backgroundColor = MaterialTheme.colors.surface,
    contentPadding = LocalWindowInsets.current.systemBars.toPaddingValues()
) {
    Column {
        Text(text = "John Smith")
        Text(text = "Online")
    }
}

@Composable
fun ChatBottomBar(
    chatMessage: String = "",
    onChatMessageEntered: OnChatMessageEntered = {},
    onSendButtonClicked: OnSendButtonClicked = {}
) = BottomAppBar(
    modifier = Modifier.wrapContentHeight(),
    backgroundColor = MaterialTheme.colors.surface,
    contentPadding = PaddingValues(
        start = SPACING_16.dp,
        end = SPACING_16.dp,
        bottom = SPACING_8.dp
    ),
    content = {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding(),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = null,
                    modifier = Modifier.clickable(onClick = onSendButtonClicked)
                )
            },
            value = chatMessage,
            onValueChange = onChatMessageEntered,
            keyboardActions = KeyboardActions(onSend = { onSendButtonClicked() }),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Send
            )
        )
    }
)

@Composable
fun ChatMessageItem() = Row(modifier = Modifier.fillMaxWidth()) {
    Text(
        text = LoremIpsum().values.joinToString(),
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.primary)
            .clip(shape = RoundedCornerShape(size = SPACING_8.dp))
            .padding(all = SPACING_8.dp)
    )
}
