package com.spaceapps.myapplication.features.conversations

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.SPACING_64
import com.spaceapps.myapplication.ui.SPACING_8
import com.spaceapps.myapplication.ui.SpaceAppsTheme

data class Conversation(
    val imageUrl: String? = null,
    val name: String = LoremIpsum(15).values.joinToString(),
    val lastMessage: String = LoremIpsum(25).values.joinToString()
)

@OptIn(ExperimentalStdlibApi::class)
val conversations = buildList {
    repeat(50) {
        add(Conversation())
    }
}

@Preview
@Composable
fun ConversationsScreenPreview() = SpaceAppsTheme {
    ConversationsScreen()
}

@Composable
fun ConversationsScreen() = Scaffold(
    backgroundColor = MaterialTheme.colors.background,
    floatingActionButton = { CreateConversationFab() }
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(SPACING_8.dp)
    ) {
        items(conversations) { conversation ->
            ConversationItem(
                imageUrl = conversation.imageUrl,
                name = conversation.name,
                lastMessage = conversation.lastMessage
            )
        }
    }
}

@Preview
@Composable
fun ConversationItemPreview() = SpaceAppsTheme {
    ConversationItem(
        imageUrl = null,
        name = "John Smith",
        lastMessage = "Lorem ipsum dolor"
    )
}

@Composable
fun ConversationItem(imageUrl: String?, name: String, lastMessage: String) =
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = SPACING_8.dp)
        ) {
            val painter =
                rememberCoilPainter(request = imageUrl, previewPlaceholder = R.drawable.ic_person)
            Image(
                modifier = Modifier
                    .size(SPACING_64.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterVertically),
                painter = painter,
                contentDescription = "Avatar"
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(text = name, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = lastMessage, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }

@Composable
fun CreateConversationFab() = FloatingActionButton(onClick = {}) {
    Icon(painter = painterResource(R.drawable.ic_create), contentDescription = "Create")
}