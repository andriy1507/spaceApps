package com.spaceapps.myapplication.features.notifications

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.OnClick
import com.spaceapps.myapplication.ui.SPACING_128
import com.spaceapps.myapplication.ui.SPACING_16
import com.spaceapps.myapplication.ui.SPACING_4
import com.spaceapps.myapplication.utils.plus
import kotlinx.coroutines.flow.collect

@Composable
fun NotificationsScreen(vm: NotificationsViewModel) {
    val scaffoldState = rememberScaffoldState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val events = remember(vm.events, lifecycleOwner) {
        vm.events.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
    }
    val context = LocalContext.current
    val notifications = vm.notifications.collectAsLazyPagingItems()
    LaunchedEffect(events) {
        events.collect {
            when (it) {
                is NotificationsEvent.ShowSnackBar ->
                    scaffoldState.snackbarHostState
                        .showSnackbar(context.getString(it.messageId))
            }
        }
    }
    val statusBarPadding =
        rememberInsetsPaddingValues(insets = LocalWindowInsets.current.statusBars)
    val navigationBarPadding =
        rememberInsetsPaddingValues(insets = LocalWindowInsets.current.navigationBars)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                contentPadding = AppBarDefaults.ContentPadding + statusBarPadding
            ) {
                IconButton(onClick = vm::goBack) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                }
                Text(
                    text = stringResource(id = R.string.notifications),
                    style = MaterialTheme.typography.h6
                )
            }
        }
    ) {
        val swipeRefreshState =
            rememberSwipeRefreshState(isRefreshing = notifications.loadState.refresh is LoadState.Loading)
        SwipeRefresh(
            modifier = Modifier.fillMaxSize(),
            state = swipeRefreshState,
            onRefresh = notifications::refresh,
            indicator = { state, trigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = trigger,
                    contentColor = MaterialTheme.colors.primary
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = navigationBarPadding
            ) {
                itemsIndexed(notifications, key = { _, n -> n.id }) { i, n ->
                    NotificationItem(
                        title = n?.title,
                        text = n?.text,
                        onClick = { n?.let { vm.goNotificationView(it.id, it.title) } },
                        onDismiss = { n?.let { vm.deleteNotification(it.id) } }
                    )
                    if (i != notifications.itemCount - 1) Divider()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun NotificationItem(
    modifier: Modifier = Modifier,
    title: String?,
    text: String?,
    onClick: OnClick,
    onDismiss: OnClick
) {
    val dismissState = rememberDismissState()
    val backgroundColor = when (dismissState.dismissDirection) {
        DismissDirection.EndToStart -> Color.Red
        else -> MaterialTheme.colors.background
    }
    if (dismissState.isDismissed(DismissDirection.EndToStart)) SideEffect(onDismiss)
    AnimatedVisibility(
        visible = !dismissState.isDismissed(DismissDirection.EndToStart),
        enter = EnterTransition.None,
        exit = shrinkVertically(Alignment.CenterVertically)
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
            directions = setOf(DismissDirection.EndToStart),
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
                        modifier = Modifier.placeholder(title.isNullOrEmpty()),
                        text = title.orEmpty(),
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        modifier = Modifier
                            .placeholder(text.isNullOrEmpty())
                            .weight(1f),
                        text = text.orEmpty(),
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        )
    }
}
