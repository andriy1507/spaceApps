package com.spaceapps.myapplication.features.feedView

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.spaceapps.myapplication.models.remote.feeds.FeedFullResponse
import dev.chrisbanes.accompanist.insets.systemBarsPadding
import java.time.format.DateTimeFormatter

private const val DATE_FORMAT = "dd-MMM-yyyy"

@Composable
fun FeedViewScreen(vm: FeedViewViewModel) = Box(modifier = Modifier.fillMaxSize()) {
    val feed by vm.feed.observeAsState()
    if (feed == null) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    } else {
        FeedView(modifier = Modifier.systemBarsPadding(), feed = feed!!)
    }
}

@Composable
fun FeedView(modifier: Modifier = Modifier, feed: FeedFullResponse) = Column(modifier = modifier) {
    Text(feed.title)
    Divider()
    Text(DateTimeFormatter.ofPattern(DATE_FORMAT).format(feed.created))
    Divider()
    feed.items.forEach {
        Text(it.text)
    }
}
