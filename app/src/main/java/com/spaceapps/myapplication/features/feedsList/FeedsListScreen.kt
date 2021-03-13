package com.spaceapps.myapplication.features.feedsList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.models.remote.feeds.FeedResponse
import com.spaceapps.myapplication.ui.ACTION_BAR_SIZE
import com.spaceapps.myapplication.ui.SPACING_4
import com.spaceapps.myapplication.ui.SPACING_48
import com.spaceapps.myapplication.ui.SPACING_8
import dev.chrisbanes.accompanist.insets.LocalWindowInsets
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import dev.chrisbanes.accompanist.insets.toPaddingValues

@Composable
fun FeedsListScreen(vm: FeedsListViewModel) =
    Column(modifier = Modifier.fillMaxSize()) {
        val feedList by vm.feeds.observeAsState(initial = emptyList())
        FeedsTopBar(
            modifier = Modifier.statusBarsHeight(ACTION_BAR_SIZE.dp),
            onAddClicked = vm::goCreateFeed
        )
        LazyColumn(
            contentPadding = LocalWindowInsets.current.navigationBars.toPaddingValues(
                additionalEnd = SPACING_8.dp,
                additionalStart = SPACING_8.dp,
                additionalTop = SPACING_4.dp,
                additionalBottom = SPACING_4.dp,
            )
        ) {
            items(feedList) {
                FeedItem(it, vm::toggleFeedLike, vm::goComments)
            }
        }
    }

@Composable
fun FeedItem(feed: FeedResponse, onLikeClick: (Int) -> Unit, onCommentClick: (Int) -> Unit) =
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = SPACING_4.dp)) {
        Column {
            feed.items.forEach {
                Text(text = it.text)
            }
            FeedSocialItem(
                onLikeClick = { onLikeClick(feed.id) },
                onCommentClick = { onCommentClick(feed.id) }
            )
        }
    }

@Composable
fun FeedSocialItem(onLikeClick: () -> Unit, onCommentClick: () -> Unit) = Row {
    Icon(
        painter = painterResource(id = R.drawable.ic_apple),
        contentDescription = null,
        modifier = Modifier.clickable(onClick = onLikeClick)
    )
    Icon(
        painter = painterResource(id = R.drawable.ic_apple),
        contentDescription = null,
        modifier = Modifier.clickable(onClick = onCommentClick)
    )
}

@Composable
fun FeedsTopBar(modifier: Modifier = Modifier, onAddClicked: () -> Unit) = TopAppBar(
    modifier = modifier.fillMaxWidth(),
    contentPadding = LocalWindowInsets.current.statusBars.toPaddingValues(
        additionalStart = SPACING_8.dp,
        additionalEnd = SPACING_8.dp
    )
) {
    Text(
        text = stringResource(R.string.feeds),
        modifier = Modifier.align(Alignment.CenterVertically)
    )
    Spacer(modifier = Modifier.weight(1f))
    Icon(
        painter = painterResource(id = R.drawable.ic_add),
        contentDescription = null,
        modifier = Modifier
            .clickable(onClick = onAddClicked)
            .align(Alignment.CenterVertically)
            .size(SPACING_48.dp)
    )
}
