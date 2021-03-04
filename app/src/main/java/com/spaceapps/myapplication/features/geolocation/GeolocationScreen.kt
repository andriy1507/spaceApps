package com.spaceapps.myapplication.features.geolocation

import android.location.Location
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.ACTION_BAR_SIZE
import com.spaceapps.myapplication.ui.SPACING_16
import com.spaceapps.myapplication.ui.SPACING_8
import com.spaceapps.myapplication.ui.views.GoogleMap
import com.spaceapps.myapplication.utils.calculateRectangularCoordinates
import com.spaceapps.myapplication.utils.latitudeString
import com.spaceapps.myapplication.utils.longitudeString
import dev.chrisbanes.accompanist.insets.LocalWindowInsets
import dev.chrisbanes.accompanist.insets.statusBarsPadding
import dev.chrisbanes.accompanist.insets.toPaddingValues

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GeolocationScreen(fragmentManager: FragmentManager) {
    val viewModel = viewModel<GeolocationViewModel>()
    val location = viewModel.lastLocation.observeAsState(null)
    val events = viewModel.events.observeAsState(InitState)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .statusBarsPadding()
            .padding(horizontal = SPACING_16.dp)
    ) {
        val loc = location.value
        if (loc == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                GeoCoordinatesCard(loc)
                RectCoordinatesCard(loc)
                AnimatedVisibility(visible = events.value == LocationUnavailable) {
                    LocationUnavailable()
                }

                GoogleMap(
                    manager = fragmentManager,
                    onMapLoaded = {
                        val latLng = LatLng(loc.latitude, loc.longitude)
                        val marker = MarkerOptions().position(latLng)
                        it.clear()
                        it.addMarker(marker)
                        val accuracy = loc.accuracy
                        val zoom = when {
                            accuracy < 200 -> 17.5f
                            else -> 15f
                        }
                        it.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            paddingValues = LocalWindowInsets.current.navigationBars
                                .toPaddingValues(additionalBottom = (ACTION_BAR_SIZE + SPACING_8).dp)
                        )
                        .clip(RoundedCornerShape(size = SPACING_16.dp))
                        .background(color = MaterialTheme.colors.surface)
                )
            }
        }
    }
}

@Composable
fun LocationUnavailable() = Row {
    Icon(
        modifier = Modifier.padding(end = SPACING_8.dp),
        painter = painterResource(id = R.drawable.ic_location_disabled),
        tint = MaterialTheme.colors.error,
        contentDescription = null
    )
    Text(
        text = stringResource(R.string.location_unavailable),
        color = MaterialTheme.colors.error
    )
}

@Composable
@Suppress("NamedArguments")
fun GeoCoordinatesCard(loc: Location) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = SPACING_8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(SPACING_16.dp),
        elevation = SPACING_8.dp
    ) {
        Row {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = SPACING_16.dp)
            ) {
                LabelValueText(
                    label = stringResource(R.string.latitude_label),
                    value = latitudeString(loc.latitude)
                )
                LabelValueText(
                    label = stringResource(R.string.longitude_label),
                    value = longitudeString(loc.longitude)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = SPACING_16.dp)
            ) {
                LabelValueText(
                    label = stringResource(R.string.altitude_label),
                    value = stringResource(R.string.altitude_value, loc.altitude)
                )
                LabelValueText(
                    label = stringResource(R.string.accuracy_label),
                    value = stringResource(R.string.distance_meters, loc.accuracy)
                )
            }
        }
    }
}

@Composable
fun ColumnScope.LabelValueText(label: String, value: String) {
    LabelText(
        modifier = Modifier.align(Alignment.Start),
        text = label
    )
    ValueText(
        modifier = Modifier.align(Alignment.Start),
        text = value
    )
}

@Composable
fun LabelText(modifier: Modifier = Modifier, text: String) {
    val labelStyle = LocalTextStyle.current.copy(
        color = MaterialTheme.colors.primary,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = text,
        style = labelStyle,
        modifier = modifier
            .padding(vertical = SPACING_8.dp)
    )
}

@Composable
fun ValueText(modifier: Modifier = Modifier, text: String) {
    Text(
        text = text,
        modifier = modifier.padding(bottom = SPACING_8.dp)
    )
}

@Composable
@Suppress("NamedArguments")
fun RectCoordinatesCard(loc: Location) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = SPACING_8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(SPACING_16.dp),
        elevation = SPACING_8.dp
    ) {
        val rect = calculateRectangularCoordinates(loc.longitude, loc.latitude, loc.altitude)
        Column(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(start = SPACING_16.dp)
        ) {
            Text(
                text = "X: ${rect.x}",
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(vertical = SPACING_8.dp)
            )
            Text(
                text = "Y: ${rect.y}",
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(vertical = SPACING_8.dp)
            )
            Text(
                text = "Z: ${rect.z}",
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(vertical = SPACING_8.dp)
            )
        }
    }
}
