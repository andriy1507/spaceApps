package com.spaceapps.myapplication.features.constraintLayout

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import com.google.accompanist.insets.systemBarsPadding
import com.spaceapps.myapplication.R

@OptIn(ExperimentalMotionApi::class)
@Composable
fun YoutubeAnimationScreen() {
    val sceneJson = rememberMotionScene(LocalContext.current, R.raw.youtube_motion_scene)
    val motionScene = MotionScene(sceneJson)
    val expanded = ConstraintSet(motionScene.getConstraintSet("expanded").orEmpty())
    val collapsed = ConstraintSet(motionScene.getConstraintSet("collapsed").orEmpty())
    var isCollapsed by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(if (isCollapsed) 1f else 0f, tween(500))
    MotionLayout(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        start = expanded,
        end = collapsed,
        progress = progress
    ) {
        Box(
            modifier = Modifier
                .layoutId("image")
                .background(Color.Red)
                .clickable { isCollapsed = !isCollapsed },
        )
        Text(text = "Some funny video", modifier = Modifier.layoutId("title"))
    }
}
