package com.spaceapps.myapplication.features.geolocation

import android.location.Location
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap.*
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.*
import com.spaceapps.myapplication.ui.views.GoogleMap
import com.spaceapps.myapplication.utils.LocalResources

const val MOCK_PROVIDER = "MOCK_PROVIDER"
typealias OnMapTrackingChange = (Boolean) -> Unit
typealias OnMapTypeChange = (Int) -> Unit

private const val DIALOG_WIDTH_RATIO = .75f

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GeolocationScreen(
    location: Location,
    isMapTracking: Boolean,
    onMapTrackingChange: OnMapTrackingChange,
    mapType: Int,
    onMapTypeChange: OnMapTypeChange
) = Scaffold(
    modifier = Modifier
        .fillMaxSize()
        .navigationBarsPadding()
        .padding(bottom = ACTION_BAR_SIZE.dp)
        .background(MaterialTheme.colors.background),
    floatingActionButton = {
        AnimatedVisibility(visible = !isMapTracking, enter = fadeIn(), exit = fadeOut()) {
            MapTrackingFab(onClick = { onMapTrackingChange(true) })
        }
    }
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (location.provider == MOCK_PROVIDER) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            MapPreview(
                location = location,
                isMapTracking = isMapTracking,
                onMapTrackingChange = onMapTrackingChange,
                mapType = mapType
            )
            MapToolbar(mapType, onMapTypeChange)
        }
    }
}

@Composable
fun MapToolbar(mapType: Int, onMapTypeChange: OnMapTypeChange) = Box(
    modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .statusBarsPadding()
) {
    var isExpanded by remember { mutableStateOf(false) }
    IconButton(modifier = Modifier.align(Alignment.CenterEnd), onClick = { isExpanded = true }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_map_layers),
            contentDescription = null,
            tint = if (mapType == MAP_TYPE_NORMAL) Color.DarkGray else Color.White
        )
        if (isExpanded) {
            MapTypeDialog(
                mapType = mapType,
                onDismiss = { isExpanded = false },
                onMapTypeChange = onMapTypeChange
            )
        }
    }
}

@Composable
fun MapTypeDialog(
    mapType: Int,
    onDismiss: OnClick,
    onMapTypeChange: OnMapTypeChange
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth(DIALOG_WIDTH_RATIO)
                .clip(RoundedCornerShape(SPACING_16.dp))
                .background(MaterialTheme.colors.background)
                .padding(SPACING_16.dp)
        ) {
            Row(modifier = Modifier.padding(vertical = SPACING_8.dp)) {
                RadioButton(
                    selected = mapType == MAP_TYPE_SATELLITE,
                    onClick = {
                        onMapTypeChange(MAP_TYPE_SATELLITE)
                        onDismiss()
                    }
                )
                Text(text = stringResource(R.string.satellite))
            }
            Row(modifier = Modifier.padding(vertical = SPACING_8.dp)) {
                RadioButton(
                    selected = mapType == MAP_TYPE_HYBRID,
                    onClick = {
                        onMapTypeChange(MAP_TYPE_HYBRID)
                        onDismiss()
                    }
                )
                Text(text = stringResource(R.string.hybrid))
            }
            Row(modifier = Modifier.padding(vertical = SPACING_8.dp)) {
                RadioButton(
                    selected = mapType == MAP_TYPE_NORMAL,
                    onClick = {
                        onMapTypeChange(MAP_TYPE_NORMAL)
                        onDismiss()
                    }
                )
                Text(text = stringResource(R.string.normal))
            }
        }
    }
}

@Composable
fun MapTrackingFab(
    onClick: OnClick
) = FloatingActionButton(onClick = onClick) {
    Icon(painter = painterResource(id = R.drawable.ic_location), contentDescription = null)
}

@Composable
fun MapPreview(
    location: Location,
    isMapTracking: Boolean,
    onMapTrackingChange: OnMapTrackingChange,
    mapType: Int
) {
    val density = LocalResources.current.displayMetrics.density
    val circleStrokeColor = MaterialTheme.colors.primary.copy(alpha = .85f).toArgb()
    val circleFillColor = MaterialTheme.colors.primary.copy(alpha = .25f).toArgb()
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        onMapLoaded = {
            if (it.mapType != mapType) it.mapType = mapType
            it.setOnCameraMoveStartedListener { reason ->
                if (reason == REASON_GESTURE) onMapTrackingChange(false)
            }
            val latLng = LatLng(location.latitude, location.longitude)
            val marker = MarkerOptions().position(latLng)
            val circle = CircleOptions().center(latLng)
                .strokeColor(circleStrokeColor)
                .strokeWidth(density * SPACING_1)
                .fillColor(circleFillColor)
                .radius(location.accuracy.toDouble())
            it.clear()
            it.addCircle(circle)
            it.addMarker(marker)
            val accuracy = location.accuracy
            val zoom = when {
                accuracy < 200 -> 17.5f
                else -> 15f
            }
            if (isMapTracking) it.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    latLng,
                    zoom
                )
            )
        }
    )
}
