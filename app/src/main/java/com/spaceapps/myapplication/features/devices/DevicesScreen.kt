package com.spaceapps.myapplication.features.devices

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.utils.plus
import kotlinx.coroutines.flow.collect

@Composable
fun DevicesScreen(vm: DevicesViewModel) {
    val scaffoldState = rememberScaffoldState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val events = remember(vm.events, lifecycleOwner) {
        vm.events.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
    }
    val context = LocalContext.current
    val devices = vm.devices.collectAsLazyPagingItems()
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
                    text = stringResource(id = R.string.devices),
                    style = MaterialTheme.typography.h6
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = navigationBarPadding
        ) {
            items(devices) {
                Text(text = it?.id.toString())
            }
        }
    }
}
