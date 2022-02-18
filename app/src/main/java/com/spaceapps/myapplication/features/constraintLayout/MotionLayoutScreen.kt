package com.spaceapps.myapplication.features.constraintLayout

import android.content.Context
import androidx.annotation.RawRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import com.google.accompanist.insets.navigationBarsPadding
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.SPACING_16
import com.spaceapps.myapplication.ui.SPACING_48
import java.io.BufferedReader
import java.io.InputStreamReader

@Suppress("MagicNumber")
@OptIn(ExperimentalMotionApi::class, ExperimentalComposeUiApi::class)
@Composable
fun MotionLayoutScreen() {
    val sceneJson = rememberMotionScene(LocalContext.current, R.raw.motion_scene)
    val scene = MotionScene(content = sceneJson)
    val cs1 = remember { ConstraintSet(scene.getConstraintSet("cs1").orEmpty()) }
    val cs2 = remember { ConstraintSet(scene.getConstraintSet("cs2").orEmpty()) }
    val cs3 = remember { ConstraintSet(scene.getConstraintSet("cs3").orEmpty()) }
    val cs4 = remember { ConstraintSet(scene.getConstraintSet("cs4").orEmpty()) }
    val cs5 = remember { ConstraintSet(scene.getConstraintSet("cs5").orEmpty()) }
    var start by remember { mutableStateOf(cs5) }
    var end by remember { mutableStateOf(cs2) }
    var inTransition by remember { mutableStateOf(false) }
    val progress = remember { Animatable(0f) }
    var config by remember { mutableStateOf(0) }
    var started by remember { mutableStateOf(false) }
    if (started) {
        LaunchedEffect(config) {
            if (!inTransition) {
                inTransition = true
                progress.animateTo(1f, tween(1000))
                inTransition = false
                progress.snapTo(0f)
                when (config) {
                    0 -> {
                        start = cs5
                        end = cs2
                    }
                    1 -> {
                        start = cs2
                        end = cs3
                    }
                    2 -> {
                        start = cs3
                        end = cs4
                    }
                    3 -> {
                        start = cs4
                        end = cs1
                    }
                }
            }
        }
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        MotionLayout(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            start = start,
            end = end,
            progress = progress.value
        ) {
            Box(
                modifier = Modifier
                    .layoutId("obj")
                    .size(SPACING_48)
                    .background(Color.Red)
            )
            Box(
                modifier = Modifier
                    .layoutId("obj1")
                    .size(SPACING_48)
                    .background(Color.Green)
            )
            Box(
                modifier = Modifier
                    .layoutId("obj2")
                    .size(SPACING_48)
                    .background(motionColor("obj2", "backgroundColor"))
            )
        }
        Button(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .padding(SPACING_16),
            onClick = {
                started = true
                if (!inTransition) config = (config + 1) % 4
            },
            contentPadding = PaddingValues(SPACING_16)
        ) {
            Text(text = "Run")
        }
    }
}

@Composable
fun rememberMotionScene(context: Context, @RawRes rawRes: Int): String = remember {
    val inputStream = context.resources.openRawResource(rawRes)
    val reader = BufferedReader(InputStreamReader(inputStream))
    return@remember reader.use { r -> r.readText() }
}
