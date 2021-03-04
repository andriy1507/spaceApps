package com.spaceapps.myapplication.features.notifications

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.spaceapps.myapplication.ui.ACTION_BAR_SIZE
import com.spaceapps.myapplication.ui.SPACING_4
import com.spaceapps.myapplication.ui.SPACING_8
import com.spaceapps.myapplication.ui.SpaceAppsTheme
import dev.chrisbanes.accompanist.insets.LocalWindowInsets
import dev.chrisbanes.accompanist.insets.toPaddingValues
import kotlin.random.Random

private const val MAX_WORDS = 30
private const val MIN_WORDS = 10
private const val NOTIFICATIONS = 15

@Immutable
data class Notification(
    val title: String,
    val content: String,
    val onClick: () -> Unit
)

@Composable
fun NotificationsScreen() {
    val contentPadding = LocalWindowInsets.current.systemBars.toPaddingValues(
        additionalStart = SPACING_8.dp,
        additionalEnd = SPACING_8.dp,
        additionalTop = SPACING_4.dp,
        additionalBottom = SPACING_4.dp + ACTION_BAR_SIZE.dp
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        val context = LocalContext.current
        LazyColumn(
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(SPACING_8.dp)
        ) {
            items(
                List(NOTIFICATIONS) {
                    Notification(
                        title = "Notification",
                        content = LoremIpsum(
                            Random.nextInt(
                                from = MIN_WORDS,
                                until = MAX_WORDS
                            )
                        ).values.joinToString(),
                        onClick = {
                            Toast.makeText(context, "Not yet implemented", Toast.LENGTH_SHORT)
                                .show()
                        }
                    )
                }
            ) {
                NotificationItem(it)
            }
        }
    }
}

@Composable
fun NotificationItem(notification: Notification) = Card(
    modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .shadow(elevation = SPACING_4.dp, shape = RoundedCornerShape(SPACING_8.dp))
        .clickable(onClick = notification.onClick)
        .background(color = MaterialTheme.colors.surface)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = SPACING_4.dp, horizontal = SPACING_8.dp)
    ) {
        Text(text = notification.title, fontWeight = FontWeight.Bold)
        Text(text = notification.content)
    }
}

@Preview
@Composable
fun NotificationsScreenPreview() = SpaceAppsTheme(darkTheme = true) {
    NotificationsScreen()
}
