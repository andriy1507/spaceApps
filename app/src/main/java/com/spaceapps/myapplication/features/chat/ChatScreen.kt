package com.spaceapps.myapplication.features.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.imePadding
import com.google.accompanist.insets.toPaddingValues
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.*
import kotlin.random.Random

@Preview(device = Devices.DEFAULT)
@Composable
fun ChatScreenPreview() {
    SpaceAppsTheme {
        ChatScreen()
    }
}

@Suppress("MagicNumber")
data class ChatMessage(
    val isOwner: Boolean = Random.nextBoolean(),
    val text: String = LoremIpsum(Random.nextInt(5, 25)).values.joinToString()
)

@OptIn(ExperimentalStdlibApi::class)
private val messages = buildList {
    repeat(50) {
        add(ChatMessage())
    }
}

@Composable
fun ChatScreen() = Scaffold(
    topBar = { ChatToolbar() },
    backgroundColor = MaterialTheme.colors.background,
    bottomBar = { MessageInput() }
) {
    Column {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentPadding = PaddingValues(horizontal = SPACING_4.dp),
            reverseLayout = true
        ) {
            items(messages) {
                val modifier = Modifier.padding(vertical = SPACING_8.dp)
                when (it.isOwner) {
                    true -> SentChatMessage(
                        modifier = modifier.align(Alignment.End),
                        message = it
                    )
                    false -> ReceivedChatMessage(
                        modifier = modifier.align(Alignment.Start),
                        message = it
                    )
                }
            }
        }
    }
}

@Composable
fun ChatToolbar() = TopAppBar(
    contentPadding = LocalWindowInsets.current.statusBars.toPaddingValues()
) {
    Icon(
        modifier = Modifier
            .size(SPACING_48.dp)
            .padding(SPACING_12.dp),
        painter = painterResource(id = R.drawable.ic_back),
        contentDescription = "Back"
    )
    val painter = rememberCoilPainter(
        request = "",
        previewPlaceholder = R.drawable.ic_person
    )
    Image(
        modifier = Modifier
            .padding(vertical = SPACING_4.dp, horizontal = SPACING_8.dp)
            .size(SPACING_48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colors.background),
        painter = painter,
        contentDescription = "Avatar"
    )
    Text(text = "John Smith", style = MaterialTheme.typography.h6)
    Spacer(modifier = Modifier.weight(1f))
    Icon(
        modifier = Modifier
            .size(SPACING_48.dp)
            .padding(SPACING_12.dp),
        painter = painterResource(R.drawable.ic_call),
        contentDescription = "Call"
    )
    Icon(
        modifier = Modifier
            .size(SPACING_48.dp)
            .padding(SPACING_12.dp),
        painter = painterResource(R.drawable.ic_more_vert),
        contentDescription = "More"
    )
}

@Composable
fun SentChatMessage(modifier: Modifier, message: ChatMessage) = Box(
    modifier = modifier
        .padding(start = SPACING_48.dp)
        .wrapContentSize()
        .clip(RoundedCornerShape(SPACING_8.dp))
        .background(MaterialTheme.colors.primaryVariant)

) {
    Text(
        modifier = Modifier.padding(SPACING_4.dp),
        text = message.text,
        color = MaterialTheme.colors.onPrimary
    )
}

@Composable
fun ReceivedChatMessage(modifier: Modifier, message: ChatMessage) = Box(
    modifier = modifier
        .padding(end = SPACING_48.dp)
        .wrapContentSize()
        .clip(RoundedCornerShape(SPACING_8.dp))
        .background(MaterialTheme.colors.primarySurface)

) {
    Text(
        modifier = Modifier.padding(SPACING_4.dp),
        text = message.text,
        color = MaterialTheme.colors.onPrimary
    )
}

@Composable
fun MessageInput() {
    val bottomPadding = with(LocalWindowInsets.current) {
        if (ime.isVisible) PaddingValues(0.dp) else navigationBars.toPaddingValues()
    }
    BottomAppBar(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .imePadding(),
        contentPadding = bottomPadding
    ) {
        var message by remember { mutableStateOf("") }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            value = message,
            onValueChange = { message = it }
        )
    }
}
