package com.spaceapps.myapplication.features.geolocation

import android.location.Location
import androidx.annotation.StringRes
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap.*
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE
import com.google.android.gms.maps.model.LatLng
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.ACTION_BAR_SIZE
import com.spaceapps.myapplication.ui.OnClick
import com.spaceapps.myapplication.ui.SPACING_16
import com.spaceapps.myapplication.ui.SPACING_8
import com.spaceapps.myapplication.ui.views.GoogleMap

typealias OnMapTrackingChange = (Boolean) -> Unit
typealias OnMapTypeChange = (Int) -> Unit

data class MapType(val type: Int, @StringRes val name: Int)

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
        MapPreview(
            location = location,
            isMapTracking = isMapTracking,
            onMapTrackingChange = onMapTrackingChange,
            mapType = mapType
        )
        MapToolbar(mapType, onMapTypeChange)
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
    val types = listOf(
        MapType(MAP_TYPE_NORMAL, R.string.normal),
        MapType(MAP_TYPE_SATELLITE, R.string.satellite),
        MapType(MAP_TYPE_HYBRID, R.string.hybrid),
    )
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth(DIALOG_WIDTH_RATIO)
                .clip(RoundedCornerShape(SPACING_16.dp))
                .background(MaterialTheme.colors.background)
                .padding(SPACING_16.dp)
        ) {
            for (type in types) {
                Row(modifier = Modifier.padding(vertical = SPACING_8.dp)) {
                    RadioButton(
                        selected = mapType == type.type,
                        onClick = {
                            onMapTypeChange(type.type)
                            onDismiss()
                        }
                    )
                    Text(text = stringResource(type.name))
                }
            }
        }
    }
}

@Composable
fun MapTrackingFab(
    onClick: OnClick
) = FloatingActionButton(onClick = onClick) {
    Icon(
        painter = painterResource(R.drawable.ic_location),
        contentDescription = stringResource(R.string.location)
    )
}

@Composable
fun MapPreview(
    location: Location,
    isMapTracking: Boolean,
    onMapTrackingChange: OnMapTrackingChange,
    mapType: Int
) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        onMapLoaded = {
            if (it.mapType != mapType) it.mapType = mapType
            it.setOnCameraMoveStartedListener { reason ->
                onMapTrackingChange(reason != REASON_GESTURE)
            }
            val latLng = LatLng(location.latitude, location.longitude)
            val zoom = when {
                location.accuracy < 200 -> 17.5f
                else -> 15f
            }
            if (isMapTracking) it.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
        }
    )
}
