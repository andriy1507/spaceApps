package com.spaceapps.myapplication.features.location.map

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
import com.google.accompanist.insets.statusBarsPadding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.app.*
import com.spaceapps.myapplication.ui.*
import com.spaceapps.myapplication.ui.views.GoogleMap
import gov.nasa.worldwind.geom.Angle
import gov.nasa.worldwind.geom.coords.UTMCoord
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
    val bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Expanded)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)
    val degreesFormat by vm.degreesFormat.collectAsState()
    val coordSystem by vm.coordSystem.collectAsState()
    LaunchedEffect(Unit) {
        locationRequest.launch(arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION))
    }
    BottomSheetScaffold(
        sheetContent = {
            BottomSheetContent(
                location = location,
                degreesFormat = degreesFormat,
                coordSystem = coordSystem
            )
        },
        scaffoldState = scaffoldState,
        floatingActionButton = {
            AnimatedVisibility(visible = !isFocusMode, enter = fadeIn(), exit = fadeOut()) {
                MapTrackingFab(onClick = vm::onFocusClick)
            }
        },
        content = {
            Box(Modifier.fillMaxSize()) {
                MapPreview(location = location, isFocusMode = isFocusMode, vm::onCameraMoved)
                Button(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(SPACING_16)
                        .size(SPACING_48)
                        .align(Alignment.TopEnd),
                    onClick = vm::goToSettings,
                    contentPadding = PaddingValues(SPACING_12),
                    shape = CircleShape
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings),
                        contentDescription = null
                    )
                }
            }
        },
        sheetPeekHeight = SPACING_64,
        sheetShape = MaterialTheme.shapes.large.copy(
            bottomEnd = CornerSize(SPACING_0),
            bottomStart = CornerSize(SPACING_0)
        )
    )
}

@Composable
fun BottomSheetContent(location: Location?, degreesFormat: String, coordSystem: String) {
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
        when (coordSystem) {
            SYSTEM_GEO -> GeoCoordSystem(location = location, degreesFormat = degreesFormat)
            SYSTEM_UTM -> UtmCoordSystem(location = location)
            SYSTEM_S43 -> S43CoordSystem(location = location)
            SYSTEM_S63 -> S63CoordSystem(location = location)
            else -> throw IllegalArgumentException("Format is not supported")
        }
    }
}

@Composable
private fun GeoCoordSystem(location: Location?, degreesFormat: String) {
    Row(
        modifier = Modifier.padding(bottom = SPACING_16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.latitude_label),
            style = MaterialTheme.typography.h5
        )
        Text(
            modifier = Modifier.weight(1f),
            text = getLatitudeString(location = location, format = degreesFormat),
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
            text = stringResource(id = R.string.longitude_label),
            style = MaterialTheme.typography.h5
        )
        Text(
            modifier = Modifier.weight(1f),
            text = getLongitudeString(location = location, format = degreesFormat),
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
private fun UtmCoordSystem(location: Location?) {
    location ?: return
    val coord = UTMCoord.fromLatLon(
        Angle.fromDegreesLatitude(location.latitude),
        Angle.fromDegreesLongitude(location.longitude)
    )
    Row(
        modifier = Modifier.padding(bottom = SPACING_16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "Northing",
            style = MaterialTheme.typography.h5
        )
        Text(
            modifier = Modifier.weight(1f),
            text = "${coord.northing}",
            style = MaterialTheme.typography.body1
        )
    }
    Divider()
    Row(
        modifier = Modifier.padding(vertical = SPACING_16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "Easting",
            style = MaterialTheme.typography.h5
        )
        Text(
            modifier = Modifier.weight(1f),
            text = "${coord.easting}",
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
            text = "Zone",
            style = MaterialTheme.typography.h5
        )
        Text(
            modifier = Modifier.weight(1f),
            text = "${coord.zone}",
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
private fun S43CoordSystem(location: Location?) {
    // TODO 9/28/2021
}

@Composable
private fun S63CoordSystem(location: Location?) {
    // TODO 9/28/2021
}

@Composable
private fun getLatitudeString(location: Location?, format: String) = when (location) {
    null -> stringResource(id = R.string.n_a)
    else -> {
        val heading = when (location.latitude < 0) {
            true -> R.string.south
            false -> R.string.north
        }
        stringResource(
            id = R.string.heading,
            when (format) {
                DEGREES_DMS -> convertDecimalToDMS(value = location.latitude)
                DEGREES_DECIMAL -> stringResource(
                    id = R.string.coordinate_decimal,
                    location.latitude
                )
                else -> ""
            },
            stringResource(id = heading)
        )
    }
}

@Composable
private fun getLongitudeString(location: Location?, format: String) = when (location) {
    null -> stringResource(id = R.string.n_a)
    else -> {
        val heading = when (location.longitude < 0) {
            true -> R.string.west
            false -> R.string.east
        }
        stringResource(
            id = R.string.heading,
            when (format) {
                DEGREES_DMS -> convertDecimalToDMS(value = location.longitude)
                DEGREES_DECIMAL -> stringResource(
                    id = R.string.coordinate_decimal,
                    location.longitude
                )
                else -> ""
            },
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
