package com.spaceapps.myapplication.features.player

import android.content.ComponentName
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerView
import com.google.common.util.concurrent.MoreExecutors
import com.spaceapps.myapplication.app.services.PlayerService

@Composable
@UnstableApi
fun PlayerScreen(viewModel: PlayerViewModel) {
    val context = LocalContext.current
    val sessionToken = remember {
        SessionToken(context, ComponentName(context, PlayerService::class.java))
    }
    val playerView = remember { PlayerView(context) }
    val mediaControllerFuture =
        remember { MediaController.Builder(context, sessionToken).buildAsync() }
    LaunchedEffect(Unit) {
        mediaControllerFuture.addListener(
            { playerView.player = mediaControllerFuture.get() },
            MoreExecutors.directExecutor()
        )
    }
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        factory = { playerView }
    )
}
