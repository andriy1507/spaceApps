package com.spaceapps.myapplication.ui.views

import android.view.View
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat

@Composable
fun CameraPreview(modifier: Modifier = Modifier, imageCapture: ImageCapture) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraPreview = remember {
        PreviewView(context).apply {
            id = View.generateViewId()
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }
    val cameraProviderFeature = remember { ProcessCameraProvider.getInstance(context) }
    val executor = remember { ContextCompat.getMainExecutor(context) }
    val preview = remember {
        Preview.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_16_9)
            .build().also { it.setSurfaceProvider(cameraPreview.surfaceProvider) }
    }

    val cameraSelector = remember { CameraSelector.DEFAULT_BACK_CAMERA }
    DisposableEffect(Unit) {
        val cameraProvider = cameraProviderFeature.get()
        cameraProviderFeature.addListener(
            {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            },
            executor
        )

        onDispose { cameraProvider.unbindAll() }
    }
    AndroidView(modifier = modifier, factory = { cameraPreview })
}
