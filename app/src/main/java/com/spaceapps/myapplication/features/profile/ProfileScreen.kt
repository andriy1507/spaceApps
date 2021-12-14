package com.spaceapps.myapplication.features.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import kotlinx.coroutines.flow.collect

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val scaffoldState = rememberScaffoldState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
    }
    val context = LocalContext.current
    LaunchedEffect(events) {
        events.collect {
            when (it) {
                is ProfileEvent.ShowSnackBar ->
                    scaffoldState.snackbarHostState
                        .showSnackbar(context.getString(it.messageId))
            }
        }
    }
    val statusBarPadding =
        rememberInsetsPaddingValues(insets = LocalWindowInsets.current.statusBars)
    Scaffold(modifier = Modifier.fillMaxSize(), scaffoldState = scaffoldState) {
        LazyColumn(Modifier.fillMaxSize(), contentPadding = statusBarPadding) {
            item {
                Button(onClick = viewModel::goDevices) {
                    Text("Devices")
                }
            }
            item {
                Button(onClick = viewModel::goNotifications) {
                    Text("Notifications")
                }
            }
            item {
                Button(onClick = viewModel::goPlayer) {
                    Text("Player")
                }
            }
        }
    }
}
