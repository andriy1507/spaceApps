package com.spaceapps.myapplication.features.location.map

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.location.Location
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.core.*
import com.spaceapps.myapplication.features.location.map.GeolocationMapAction.*
import com.spaceapps.myapplication.ui.*
import gov.nasa.worldwind.geom.Angle
import gov.nasa.worldwind.geom.coords.UTMCoord
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(
    ExperimentalMaterialApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun GeolocationMapScreen(viewModel: GeolocationMapViewModel) {
    val state by viewModel.state.collectAsState()
    val locationPermissionState = rememberPermissionState(ACCESS_FINE_LOCATION)
    val lifecycleOwner = LocalLifecycleOwner.current
    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
    }
    val context = LocalContext.current
    val bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Expanded)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)
    LaunchedEffect(Unit) {
        events.onEach { event ->
            when (event) {
                is GeolocationMapEvent.ShowSnackBar -> {
                    val message = context.getString(event.messageId)
                    scaffoldState.snackbarHostState.showSnackbar(message)
                }
            }
        }.launchIn(this)
    }
    PermissionRequired(
        permissionState = locationPermissionState,
        permissionNotGrantedContent = {
            PermissionRationaleDialog(onDismiss = locationPermissionState::launchPermissionRequest)
        },
        permissionNotAvailableContent = {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(modifier = Modifier.align(Alignment.Center), text = stringResource(R.string.location_not_available))
            }
        }
    ) {
        LaunchedEffect(Unit) { viewModel.submitAction(TrackLocation) }
        BottomSheetScaffold(
            sheetContent = {
                BottomSheetContent(
                    location = state.location,
                    degreesFormat = state.degreesFormat,
                    coordSystem = state.coordSystem
                )
            },
            scaffoldState = scaffoldState,
            floatingActionButton = {
                MapMultiActionFab(
                    isLocationVisible = state.isFocusMode,
                    onFocusClick = { viewModel.submitAction(FocusClicked) },
                    onListClick = { viewModel.submitAction(GoToLocationsList) },
                    onAddClick = { viewModel.submitAction(AddLocation(state.location)) }
                )
            },
            content = {
                Box(Modifier.fillMaxSize()) {
                    val location = state.location
                    val cameraPositionState = rememberCameraPositionState()
                    LaunchedEffect(state) {
                        if (state.isFocusMode && location != null) {
                            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                LatLng(location.latitude, location.longitude),
                                DEFAULT_MAP_ZOOM
                            )
                            cameraPositionState.animate(cameraUpdate)
                        }
                    }
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState
                    ) {
                        if (location != null) {
                            Marker(position = LatLng(location.latitude, location.longitude))
                        }
                    }
                    Button(
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(SPACING_16)
                            .size(SPACING_48)
                            .align(Alignment.TopEnd),
                        onClick = { viewModel.submitAction(GoToSettings) },
                        contentPadding = PaddingValues(SPACING_12),
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
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
}

@Composable
fun PermissionRationaleDialog(onDismiss: OnClick) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = android.R.string.ok))
            }
        },
        title = { Text(text = stringResource(R.string.access_to_location)) },
        text = { Text(text = stringResource(R.string.location_permission_rationale_message)) },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    )
}

@Composable
fun MapMultiActionFab(
    isLocationVisible: Boolean,
    onFocusClick: OnClick,
    onListClick: OnClick,
    onAddClick: OnClick
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(SPACING_8)
    ) {
        AnimatedVisibility(visible = !isLocationVisible, enter = fadeIn(), exit = fadeOut()) {
            FloatingActionButton(onClick = onFocusClick) {
                Icon(
                    imageVector = Icons.Filled.MyLocation,
                    contentDescription = stringResource(R.string.location)
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(SPACING_8)) {
            FloatingActionButton(onClick = onListClick) {
                Icon(
                    imageVector = Icons.Filled.List,
                    contentDescription = null
                )
            }
            FloatingActionButton(onClick = onAddClick) {
                Icon(
                    imageVector = Icons.Filled.Create,
                    contentDescription = null
                )
            }
        }
    }
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
            SYSTEM_S43 -> S43CoordSystem()
            SYSTEM_S63 -> S63CoordSystem()
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
            text = stringResource(R.string.northing),
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
            text = stringResource(R.string.easting),
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
            text = stringResource(R.string.zone),
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
private fun S43CoordSystem() {
    // TODO 9/28/2021
}

@Composable
private fun S63CoordSystem() {
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
private fun convertDecimalToDMS(value: Double): String {
    val degrees = value.toInt()
    val minutes = (value - degrees) * MINUTES_IN_DEGREE
    val seconds = (minutes - minutes.toInt()) * SECONDS_IN_DEGREE / MINUTES_IN_DEGREE
    return stringResource(id = R.string.coordinate_dms, degrees, minutes.toInt(), seconds)
}
