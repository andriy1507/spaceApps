package com.spaceapps.myapplication.features.notifications

import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.core.models.local.notifications.NotificationEntity
import com.spaceapps.myapplication.coreui.OnClick
import com.spaceapps.myapplication.coreui.SPACING_128
import com.spaceapps.myapplication.coreui.SPACING_16
import com.spaceapps.myapplication.coreui.SPACING_4
import com.spaceapps.myapplication.utils.plus
import kotlinx.coroutines.flow.Flow

@Composable
fun NotificationsScreen(viewModel: NotificationsViewModel) {
    val scaffoldState = rememberScaffoldState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
    }
    val context = LocalContext.current
    val notifications = viewModel.notifications.collectAsLazyPagingItems()
    ObserveEvents(events, scaffoldState, context)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = { NotificationsTopBar(viewModel::goBack) }
    ) {
        NotificationsContent(
            notificationsPagingItems = notifications,
            onNotificationClick = viewModel::goNotificationView,
            onDelete = viewModel::deleteNotification
        )
    }
}

@Composable
private fun NotificationsTopBar(onNavigationClick: OnClick) {
    val statusBarPadding =
        rememberInsetsPaddingValues(insets = LocalWindowInsets.current.statusBars)
    TopAppBar(contentPadding = AppBarDefaults.ContentPadding + statusBarPadding) {
        IconButton(onClick = onNavigationClick) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
        }
        Text(
            text = stringResource(id = R.string.notifications),
            style = MaterialTheme.typography.h6
        )
    }
}

@Composable
private fun ObserveEvents(
    events: Flow<NotificationsEvent>,
    scaffoldState: ScaffoldState,
    context: Context
) {
    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                is NotificationsEvent.ShowSnackBar ->
                    scaffoldState.snackbarHostState
                        .showSnackbar(context.getString(event.messageId))
            }
        }
    }
}

@Composable
fun NotificationsContent(
    notificationsPagingItems: LazyPagingItems<NotificationEntity>,
    onNotificationClick: (Int, String) -> Unit,
    onDelete: (Int) -> Unit
) {
    val isRefreshing = notificationsPagingItems.loadState.refresh is LoadState.Loading
    val swipeRefreshState =
        rememberSwipeRefreshState(isRefreshing = isRefreshing && notificationsPagingItems.itemCount > 0)
    val navigationBarPadding =
        rememberInsetsPaddingValues(insets = LocalWindowInsets.current.navigationBars)
    SwipeRefresh(
        modifier = Modifier.fillMaxSize(),
        state = swipeRefreshState,
        onRefresh = notificationsPagingItems::refresh,
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = trigger,
                contentColor = MaterialTheme.colors.primary
            )
        }
    ) {
        Crossfade(isRefreshing && notificationsPagingItems.itemCount == 0) {
            when (it) {
                true -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
                false -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = navigationBarPadding
                    ) {
                        item {
                            NotificationListRetryItem(
                                state = notificationsPagingItems.loadState.prepend,
                                onRetry = notificationsPagingItems::refresh
                            )
                        }
                        itemsIndexed(notificationsPagingItems, key = { _, n -> n.id }) { i, n ->
                            NotificationItem(
                                title = n?.title,
                                text = n?.text,
                                onClick = {
                                    n?.let { onNotificationClick(n.id, n.title) }
                                }
                            ) { n?.let { onDelete(n.id) } }
                            if (i != notificationsPagingItems.itemCount - 1) Divider()
                        }
                        item {
                            NotificationListRetryItem(
                                state = notificationsPagingItems.loadState.append,
                                onRetry = notificationsPagingItems::refresh
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationListRetryItem(state: LoadState, onRetry: OnClick) {
    if (state !is LoadState.NotLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = SPACING_16),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is LoadState.Loading -> CircularProgressIndicator()
                is LoadState.Error -> Button(onClick = onRetry) {
                    Text(text = stringResource(R.string.retry))
                    Icon(imageVector = Icons.Filled.Refresh, contentDescription = null)
                }
                else -> Unit
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NotificationItem(
    modifier: Modifier = Modifier,
    title: String?,
    text: String?,
    onClick: OnClick,
    onDismiss: OnClick
) {
    val notificationTitleWidth = .75f
    val dismissState = rememberDismissState()
    val backgroundColor = when (dismissState.dismissDirection) {
        DismissDirection.EndToStart -> Color.Red
        else -> MaterialTheme.colors.background
    }
    if (dismissState.isDismissed(DismissDirection.EndToStart)) SideEffect(onDismiss)
    AnimatedVisibility(
        visible = !dismissState.isDismissed(DismissDirection.EndToStart),
        enter = EnterTransition.None,
        exit = shrinkVertically(shrinkTowards = Alignment.CenterVertically)
    ) {
        SwipeToDismiss(
            state = dismissState,
            background = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        modifier = Modifier.padding(SPACING_16),
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null
                    )
                }
            },
            directions = when {
                title == null || text == null -> emptySet()
                else -> setOf(DismissDirection.EndToStart)
            },
            dismissContent = {
                Column(
                    modifier = modifier
                        .clickable(onClick = onClick)
                        .height(SPACING_128)
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.surface)
                        .padding(horizontal = SPACING_16, vertical = SPACING_4)
                ) {
                    Text(
                        modifier = Modifier
                            .placeholder(
                                visible = title == null,
                                highlight = PlaceholderHighlight.fade(),
                                shape = RoundedCornerShape(SPACING_4)
                            )
                            .fillMaxWidth(notificationTitleWidth),
                        text = title.orEmpty(),
                        style = MaterialTheme.typography.subtitle1,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(SPACING_4))
                    Text(
                        modifier = Modifier
                            .placeholder(
                                visible = text == null,
                                highlight = PlaceholderHighlight.fade(),
                                shape = RoundedCornerShape(SPACING_4)
                            )
                            .wrapContentHeight(),
                        text = text.orEmpty(),
                        style = MaterialTheme.typography.body2,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 5
                    )
                }
            }
        )
    }
}
