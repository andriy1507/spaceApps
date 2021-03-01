package com.spaceapps.myapplication.features.geolocation

import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.models.InitState
import com.spaceapps.myapplication.models.LocationUnavailable
import com.spaceapps.myapplication.ui.SPACING_16
import com.spaceapps.myapplication.ui.SPACING_8
import com.spaceapps.myapplication.utils.calculateRectangularCoordinates
import com.spaceapps.myapplication.utils.latitudeString
import com.spaceapps.myapplication.utils.longitudeString
import dev.chrisbanes.accompanist.insets.statusBarsPadding

@Composable
fun GeolocationScreen() {
    val viewModel = viewModel<GeolocationViewModel>()
    val location = viewModel.lastLocation.observeAsState(null)
    val events = viewModel.events.observeAsState(InitState)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .statusBarsPadding()
            .padding(horizontal = SPACING_16.dp)
    ) {
        val loc = location.value
        if (loc == null) {
            CircularProgressIndicator()
        } else {
            GeoCoordinatesCard(loc)
            RectCoordinatesCard(loc)
            if (events.value == LocationUnavailable)
                Row {
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
        }
    }
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
                LabelText(
                    text = stringResource(R.string.latitude_label),
                    modifier = Modifier.align(Alignment.Start)
                )
                ValueText(
                    text = latitudeString(loc.latitude),
                    modifier = Modifier.align(Alignment.Start)
                )
                LabelText(
                    text = stringResource(R.string.longitude_label),
                    modifier = Modifier.align(Alignment.Start)
                )
                ValueText(
                    text = longitudeString(loc.longitude),
                    modifier = Modifier.align(Alignment.Start)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = SPACING_16.dp)
            ) {
                LabelText(
                    text = stringResource(R.string.altitude_label),
                    modifier = Modifier
                        .align(Alignment.Start)
                )
                ValueText(
                    text = stringResource(R.string.altitude_value, loc.altitude),
                    modifier = Modifier.align(Alignment.Start)
                )
                LabelText(
                    modifier = Modifier.align(Alignment.Start),
                    text = stringResource(R.string.accuracy_label)
                )
                ValueText(
                    modifier = Modifier.align(Alignment.Start),
                    text = stringResource(R.string.distance_meters, loc.accuracy)
                )
            }
        }
    }
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
