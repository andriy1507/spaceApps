package com.spaceapps.myapplication.features.geolocation

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.google.accompanist.insets.navigationBarsPadding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.app.DEFAULT_MAP_ZOOM
import com.spaceapps.myapplication.app.MINUTES_IN_DEGREE
import com.spaceapps.myapplication.app.SECONDS_IN_DEGREE
import com.spaceapps.myapplication.ui.*
import com.spaceapps.myapplication.ui.views.GoogleMap
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@SuppressLint("MissingPermission")
@Composable
fun GeolocationMapScreen(vm: GeolocationMapViewModel) {
    val locationRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            when (permissions.all { it.value }) {
                true -> vm.trackLocation()
                else -> Timber.e("Permission not granted")
            }
        }
    )
    val location by vm.location.collectAsState()
    val isFocusMode by vm.isFocusMode.collectAsState()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Expanded)
    )
    LaunchedEffect(Unit) {
        locationRequest.launch(arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION))
    }
    BottomSheetScaffold(
        sheetContent = { BottomSheetContent(location = location) },
        scaffoldState = scaffoldState,
        floatingActionButton = {
            AnimatedVisibility(visible = !isFocusMode, enter = fadeIn(), exit = fadeOut()) {
                MapTrackingFab(onClick = vm::onFocusClick)
            }
        },
        content = { MapPreview(location = location, isFocusMode = isFocusMode, vm::onCameraMoved) },
        sheetPeekHeight = SPACING_64,
        sheetShape = MaterialTheme.shapes.large.copy(
            bottomEnd = CornerSize(SPACING_0),
            bottomStart = CornerSize(SPACING_0)
        )
    )
}

@Composable
fun BottomSheetContent(location: Location?) {
    Column(
        modifier = Modifier
            .padding(
                top = SPACING_16,
                start = SPACING_16,
                end = SPACING_16
            )
            .navigationBarsPadding(bottom = true)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = SPACING_12, bottom = SPACING_32)
                .size(width = SPACING_64, height = SPACING_4)
                .background(color = MaterialTheme.colors.onBackground, shape = CircleShape)
        )
        Row(
            modifier = Modifier.padding(bottom = SPACING_16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.longitude_label),
                style = MaterialTheme.typography.h5
            )
            Text(
                modifier = Modifier.weight(1f),
                text = getLongitudeString(location = location),
                style = MaterialTheme.typography.body1
            )
        }
        Divider()
        Row(
            modifier = Modifier.padding(top = SPACING_16),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = R.string.latitude_label),
                style = MaterialTheme.typography.h5
            )
            Text(
                modifier = Modifier.weight(1f),
                text = getLatitudeString(location = location),
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
private fun getLatitudeString(location: Location?) = when (location) {
    null -> stringResource(id = R.string.n_a)
    else -> {
        val heading = when (location.latitude < 0) {
            true -> R.string.south
            false -> R.string.north
        }
        stringResource(
            id = R.string.heading,
            convertDecimalToDMS(value = location.latitude),
            stringResource(id = heading)
        )
    }
}

@Composable
private fun getLongitudeString(location: Location?) = when (location) {
    null -> stringResource(id = R.string.n_a)
    else -> {
        val heading = when (location.longitude < 0) {
            true -> R.string.west
            false -> R.string.east
        }
        stringResource(
            id = R.string.heading,
            convertDecimalToDMS(value = location.longitude),
            stringResource(id = heading)
        )
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
fun MapPreview(location: Location?, isFocusMode: Boolean, onCameraMoved: () -> Unit) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        onMapLoaded = { map ->
            if (location != null) {
                val marker = MarkerOptions().position(LatLng(location.latitude, location.longitude))
                map.clear()
                map.addMarker(marker)
            }
            if (location != null && isFocusMode) {
                val cameraUpdate =
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(location.latitude, location.longitude),
                        DEFAULT_MAP_ZOOM
                    )
                map.animateCamera(cameraUpdate)
            }
            map.setOnCameraMoveStartedListener { reason ->
                if (reason == REASON_GESTURE) onCameraMoved()
            }
        }
    )
}

@Composable
private fun convertDecimalToDMS(value: Double): String {
    val degrees = value.toInt()
    val minutes = (value - degrees) * MINUTES_IN_DEGREE
    val seconds = (minutes - minutes.toInt()) * SECONDS_IN_DEGREE / MINUTES_IN_DEGREE
    return stringResource(id = R.string.coordinate_dms, degrees, minutes.toInt(), seconds)
}
