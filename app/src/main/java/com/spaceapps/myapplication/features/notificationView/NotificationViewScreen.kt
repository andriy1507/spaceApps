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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.spaceapps.myapplication.core.models.remote.notifications.NotificationBodyItem
import com.spaceapps.myapplication.ui.SPACING_16
import com.spaceapps.myapplication.ui.SPACING_8
import com.spaceapps.myapplication.utils.plus
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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
        events.onEach {
            when (it) {
                is NotificationViewEvent.ShowSnackBar ->
                    scaffoldState.snackbarHostState
                        .showSnackbar(context.getString(it.messageId))
            }
        }.launchIn(this)
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
                            NotificationBodyItem.Type.Text -> TextNotificationItem(text = it.text.orEmpty())
                            NotificationBodyItem.Type.Image -> ImageNotificationItem(it.imageUrl)
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

@Composable
fun ImageNotificationItem(imageUrl: String?) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val imageRequest = with(LocalDensity.current) {
        ImageRequest.Builder(LocalContext.current)
            .size(
                width = screenWidth.dp.roundToPx() - (SPACING_16 * 2).roundToPx(),
                height = screenHeight.dp.roundToPx()
            )
            .scale(Scale.FIT)
            .data(imageUrl)
            .transformations(RoundedCornersTransformation(SPACING_16.toPx()))
            .build()
    }
    Image(
        modifier = Modifier
            .padding(
                horizontal = SPACING_16,
                vertical = SPACING_8
            )
            .fillMaxWidth(),
        painter = rememberAsyncImagePainter(model = imageRequest),
        contentDescription = null,
        contentScale = ContentScale.FillWidth
    )
}

@Composable
fun TextNotificationItem(text: String) {
    Text(
        modifier = Modifier.padding(
            horizontal = SPACING_16,
            vertical = SPACING_8
        ),
        text = text,
        style = MaterialTheme.typography.body1
    )
}
