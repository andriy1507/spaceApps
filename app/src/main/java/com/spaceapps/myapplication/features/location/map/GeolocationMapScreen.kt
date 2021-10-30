package com.spaceapps.myapplication.features.location.map

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.app.*
import com.spaceapps.myapplication.core.*
import com.spaceapps.myapplication.ui.*
import com.spaceapps.myapplication.ui.views.GoogleMap
import gov.nasa.worldwind.geom.Angle
import gov.nasa.worldwind.geom.coords.UTMCoord
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun GeolocationMapScreen(viewModel: GeolocationMapViewModel) {
    val locationRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions -> viewModel.onPermissionsResult(permissions.all { it.value }) }
    )
    val lifecycleOwner = LocalLifecycleOwner.current
    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
    }
    val context = LocalContext.current
    val location by viewModel.location.collectAsState()
    val isFocusMode by viewModel.isFocusMode.collectAsState()
    val bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Expanded)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)
    val degreesFormat by viewModel.degreesFormat.collectAsState()
    val coordSystem by viewModel.coordSystem.collectAsState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        locationRequest.launch(arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION))
        events.collect { event ->
            when (event) {
                is GeolocationMapEvents.ShowSnackBar ->
                    scaffoldState.snackbarHostState
                        .showSnackbar(context.getString(event.messageId))
                else -> Unit
            }
        }
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
            MapMultiActionFab(
                isLocationVisible = isFocusMode,
                onFocusClick = viewModel::onFocusClick,
                onListClick = viewModel::goLocationsList,
                onAddClick = { viewModel.addLocation(location) }
            )
        },
        content = {
            Box(Modifier.fillMaxSize()) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    onMapLoaded = { map ->
                        map.setOnCameraMoveStartedListener(viewModel::onCameraMoved)
                        scope.launch {
                            events.collect {
                                when (it) {
                                    is GeolocationMapEvents.AddMarker -> {
                                        map.clear()
                                        map.addMarker(it.options)
                                    }
                                    is GeolocationMapEvents.UpdateCamera -> map.animateCamera(it.update)
                                    else -> Unit
                                }
                            }
                        }
                    }
                )
                Button(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(SPACING_16)
                        .size(SPACING_48)
                        .align(Alignment.TopEnd),
                    onClick = viewModel::goToSettings,
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MapMultiActionFab(
    isLocationVisible: Boolean,
    onFocusClick: OnClick,
    onListClick: OnClick,
    onAddClick: OnClick
) {
    Column(horizontalAlignment = Alignment.End) {
        AnimatedVisibility(visible = !isLocationVisible, enter = fadeIn(), exit = fadeOut()) {
            FloatingActionButton(onClick = onFocusClick) {
                Icon(
                    imageVector = Icons.Filled.MyLocation,
                    contentDescription = stringResource(R.string.location)
                )
            }
        }
        Spacer(modifier = Modifier.height(SPACING_8))
        Row {
            FloatingActionButton(onClick = onListClick) {
                Icon(
                    imageVector = Icons.Filled.List,
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(SPACING_8))
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
