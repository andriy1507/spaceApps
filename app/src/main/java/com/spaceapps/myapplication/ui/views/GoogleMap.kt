package com.spaceapps.myapplication.ui.views

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.buildGoogleMapOptions
import kotlinx.coroutines.launch

typealias OnMapLoaded = (GoogleMap) -> Unit

@Composable
fun GoogleMap(modifier: Modifier = Modifier, onMapLoaded: OnMapLoaded) {
    val map = rememberMapViewWithLifecycle()
    var mapInitialized by remember { mutableStateOf(false) }
    LaunchedEffect(map, mapInitialized) {
        if (mapInitialized) return@LaunchedEffect
        map.awaitMap()
        mapInitialized = true
    }
    val coroutineScope = rememberCoroutineScope()
    AndroidView(
        modifier = modifier,
        factory = { map },
        update = { mapView ->
            coroutineScope.launch {
                val googleMap = mapView.awaitMap()
                onMapLoaded(googleMap)
            }
        }
    )
}

@Composable
private fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val options = buildGoogleMapOptions {
//        mapId(stringResource(id = R.string.map_id))
    }
    val mapView = remember { MapView(context, options).apply { id = View.generateViewId() } }
    // Makes MapView follow the lifecycle of this composable
    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

@Composable
private fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver {
    val bundle = rememberSaveable(key = "mapBundle") { Bundle() }
    return remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(bundle)
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> {
                    mapView.onSaveInstanceState(bundle)
                    mapView.onStop()
                }
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }
}
