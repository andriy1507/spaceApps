package com.spaceapps.myapplication.features.feedsList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.models.remote.feeds.FeedShortResponse
import com.spaceapps.myapplication.ui.*
import dev.chrisbanes.accompanist.insets.LocalWindowInsets
import dev.chrisbanes.accompanist.insets.statusBarsHeight
import dev.chrisbanes.accompanist.insets.toPaddingValues

@Composable
fun FeedsListScreen(vm: FeedsListViewModel) =
    Box(modifier = Modifier.fillMaxSize()) {
        val feedList by vm.feeds.observeAsState()
        val event by vm.events.collectAsState(initial = InitialEvent)
        FeedsTopBar(
            modifier = Modifier.statusBarsHeight(ACTION_BAR_SIZE.dp),
            onAddClicked = vm::goCreateFeed
        )
        if (feedList == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                modifier = Modifier.padding(
                    LocalWindowInsets.current.statusBars.toPaddingValues(
                        additionalTop = ACTION_BAR_SIZE.dp
                    )
                ),
                contentPadding = LocalWindowInsets.current.navigationBars.toPaddingValues(
                    additionalEnd = SPACING_8.dp,
                    additionalStart = SPACING_8.dp,
                    additionalTop = SPACING_4.dp,
                    additionalBottom = SPACING_4.dp,
                )
            ) {
                itemsIndexed(feedList.orEmpty()) { i, item ->
                    FeedItem(
                        modifier = Modifier.clickable { vm.goFeedView(item.id) },
                        feed = item,
                        onLikeClick = vm::toggleFeedLike,
                        onCommentClick = vm::goComments
                    )
                    if (i == feedList?.lastIndex ?: 0) vm.loadFeeds()
                }
                if (event is PaginationLoading) {
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }
        }
    }

@Composable
fun FeedItem(
    modifier: Modifier = Modifier,
    feed: FeedShortResponse,
    onLikeClick: (Int) -> Unit,
    onCommentClick: (Int) -> Unit
) =
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = SPACING_4.dp)
    ) {
        Column {
            Text(text = feed.title, modifier = Modifier.padding(SPACING_12.dp))
            FeedSocialItem(
                onLikeClick = { onLikeClick(feed.id) },
                onCommentClick = { onCommentClick(feed.id) }
            )
        }
    }

@Composable
fun FeedSocialItem(onLikeClick: () -> Unit, onCommentClick: () -> Unit) = Row {
    Icon(
        painter = painterResource(id = R.drawable.ic_favorite),
        contentDescription = null,
        modifier = Modifier
            .clickable(onClick = onLikeClick)
            .size(SPACING_48.dp)
            .padding(SPACING_12.dp)
    )
    Icon(
        painter = painterResource(id = R.drawable.ic_comment),
        contentDescription = null,
        modifier = Modifier
            .clickable(onClick = onCommentClick)
            .size(SPACING_48.dp)
            .padding(SPACING_12.dp)
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
            .padding(SPACING_12.dp)
    )
}
