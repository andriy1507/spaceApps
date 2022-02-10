package com.spaceapps.myapplication.features.devices

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
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
import com.spaceapps.myapplication.core.models.remote.profile.Platform
import com.spaceapps.myapplication.ui.OnClick
import com.spaceapps.myapplication.ui.SPACING_16
import com.spaceapps.myapplication.utils.plus

@Composable
fun DevicesScreen(viewModel: DevicesViewModel) {
    val scaffoldState = rememberScaffoldState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
    }
    val context = LocalContext.current
    val devices = viewModel.devices.collectAsLazyPagingItems()
    LaunchedEffect(events) {
        events.collect {
            when (it) {
                is DevicesEvent.ShowSnackBar ->
                    scaffoldState.snackbarHostState
                        .showSnackbar(context.getString(it.messageId))
            }
        }
    }
    val statusBarPadding =
        rememberInsetsPaddingValues(insets = LocalWindowInsets.current.statusBars)
    val navigationBarPadding =
        rememberInsetsPaddingValues(insets = LocalWindowInsets.current.navigationBars)
    val swipeRefreshState =
        rememberSwipeRefreshState(isRefreshing = devices.loadState.refresh is LoadState.Loading)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                contentPadding = AppBarDefaults.ContentPadding + statusBarPadding
            ) {
                IconButton(onClick = viewModel::goBack) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                }
                Text(
                    text = stringResource(id = R.string.devices),
                    style = MaterialTheme.typography.h6
                )
            }
        }
    ) {
        SwipeRefresh(
            modifier = Modifier.fillMaxSize(),
            state = swipeRefreshState,
            onRefresh = devices::refresh,
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
                itemsIndexed(items = devices, key = { _, d -> d.id }) { i, d ->
                    val iconColor = when (d?.platform) {
                        Platform.Android -> Color.Green
                        Platform.Ios -> Color.Black
                        null -> Color.Unspecified
                    }
                    val iconRes = when (d?.platform) {
                        Platform.Android -> R.drawable.ic_android
                        Platform.Ios -> R.drawable.ic_apple
                        else -> R.drawable.ic_device_unknown
                    }
                    DeviceItem(
                        iconRes = iconRes,
                        iconColor = iconColor,
                        isPlaceholder = d == null,
                        manufacturer = d?.manufacturer.orEmpty(),
                        model = d?.model.orEmpty(),
                        osVersion = d?.osVersion.orEmpty(),
                        onDismiss = { d?.let { viewModel.deleteDevice(d.id) } }
                    )
                    if (i < devices.itemCount - 1) Divider()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeviceItem(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    iconColor: Color,
    isPlaceholder: Boolean,
    manufacturer: String,
    model: String,
    osVersion: String,
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
            directions = setOf(DismissDirection.EndToStart),
            dismissContent = {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.surface)
                ) {
                    Icon(
                        modifier = Modifier.placeholder(isPlaceholder),
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = iconColor
                    )
                    Text(
                        modifier = Modifier.placeholder(isPlaceholder),
                        text = manufacturer
                    )
                    Text(
                        modifier = Modifier.placeholder(isPlaceholder),
                        text = model
                    )
                    Text(
                        modifier = Modifier.placeholder(isPlaceholder),
                        text = osVersion
                    )
                }
            }
        )
    }
}
