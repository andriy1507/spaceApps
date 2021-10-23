package com.spaceapps.myapplication.features.location.locationsList

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.OnClick
import com.spaceapps.myapplication.ui.SPACING_16
import com.spaceapps.myapplication.ui.SPACING_64
import com.spaceapps.myapplication.utils.plus
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun LocationsListScreen(viewModel: LocationsListViewModel) {
    val pagingData by viewModel.locations.collectAsState(initial = emptyFlow())
    val locations = pagingData.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.observeAsState()
    val swipeRefreshState =
        rememberSwipeRefreshState(isRefreshing = locations.loadState.refresh is LoadState.Loading)
    val statusBarPadding =
        rememberInsetsPaddingValues(insets = LocalWindowInsets.current.statusBars)
    val isSearchEnabled by viewModel.isSearchEnabled.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                contentPadding = AppBarDefaults.ContentPadding + statusBarPadding
            ) {
                IconButton(onClick = viewModel::goBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
                if (isSearchEnabled) {
                    TextField(
                        modifier = Modifier.weight(1f),
                        value = searchQuery.orEmpty(),
                        onValueChange = viewModel::onSearchQueryEnter,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = MaterialTheme.colors.onPrimary,
                            textColor = MaterialTheme.colors.onPrimary,
                            placeholderColor = MaterialTheme.colors.onPrimary
                        ),
                        placeholder = { Text(text = stringResource(id = R.string.search)) }
                    )
                    IconButton(onClick = viewModel::onCloseSearchClicked) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null
                        )
                    }
                } else {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(id = R.string.locations),
                        style = MaterialTheme.typography.h6
                    )
                    IconButton(onClick = viewModel::onSearchClicked) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    ) {
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = locations::refresh,
            indicator = { state, trigger ->
                SwipeRefreshIndicator(
                    state = state,
                    refreshTriggerDistance = trigger,
                    contentColor = MaterialTheme.colors.primary
                )
            }
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(locations, key = { it.id }) {
                    LocationItem(
                        onDismiss = { it?.let { viewModel.deleteLocation(it.id) } },
                        name = it?.name
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun LocationItem(
    modifier: Modifier = Modifier,
    onDismiss: OnClick,
    name: String?
) {
    val dismissState = rememberDismissState()
    if (dismissState.isDismissed(DismissDirection.EndToStart)) SideEffect(onDismiss)
    val backgroundColor by animateColorAsState(
        when (dismissState.dismissDirection) {
            DismissDirection.EndToStart -> Color.Red
            else -> Color.Unspecified
        }
    )
    AnimatedVisibility(
        visible = !dismissState.isDismissed(DismissDirection.EndToStart),
        enter = EnterTransition.None,
        exit = shrinkVertically(Alignment.CenterVertically)
    ) {
        SwipeToDismiss(
            state = dismissState,
            directions = setOf(DismissDirection.EndToStart),
            background = {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(backgroundColor),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        modifier = Modifier.padding(horizontal = SPACING_16),
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null
                    )
                }
            }
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(SPACING_64)
                    .background(MaterialTheme.colors.surface),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = SPACING_16)
                        .placeholder(name == null, color = MaterialTheme.colors.onSurface),
                    text = name.orEmpty()
                )
            }
        }
    }
}
