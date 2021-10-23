package com.spaceapps.myapplication.features.notificationView

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.spaceapps.myapplication.app.models.remote.notifications.NotificationBodyItem
import com.spaceapps.myapplication.ui.SPACING_16
import com.spaceapps.myapplication.ui.SPACING_8
import com.spaceapps.myapplication.utils.plus
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalCoilApi::class)
@Composable
fun NotificationViewScreen(viewModel: NotificationViewViewModel) {
    val scaffoldState = rememberScaffoldState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
    }
    val notification by viewModel.notification.observeAsState()
    val context = LocalContext.current
    LaunchedEffect(events) {
        events.collect {
            when (it) {
                is NotificationViewEvent.ShowSnackBar ->
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
                IconButton(onClick = viewModel::goBack) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                }
                Text(
                    text = viewModel.title.orEmpty(),
                    style = MaterialTheme.typography.h6
                )
            }
        }
    ) {
        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)
        SwipeRefresh(
            modifier = Modifier.fillMaxSize(),
            state = swipeRefreshState,
            onRefresh = viewModel::refresh,
            indicator = { state, trigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = trigger,
                    contentColor = MaterialTheme.colors.primary
                )
            }
        ) {
            if (notification != null) {
                val items = notification?.body.orEmpty().sortedBy { it.index }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = navigationBarPadding
                ) {
                    items(items) {
                        when (it.type) {
                            NotificationBodyItem.Type.Text -> {
                                Text(
                                    modifier = Modifier.padding(
                                        horizontal = SPACING_16,
                                        vertical = SPACING_8
                                    ),
                                    text = it.text.orEmpty(),
                                    style = MaterialTheme.typography.body1
                                )
                            }
                            NotificationBodyItem.Type.Image -> {
                                Image(
                                    modifier = Modifier
                                        .padding(
                                            horizontal = SPACING_16,
                                            vertical = SPACING_8
                                        )
                                        .fillMaxWidth(),
                                    painter = rememberImagePainter(data = it.imageUrl) {
                                        with(LocalDensity.current) {
                                            transformations(RoundedCornersTransformation(SPACING_16.toPx()))
                                        }
                                    },
                                    contentDescription = null,
                                    contentScale = ContentScale.FillWidth
                                )
                            }
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            }
        }
    }
}
