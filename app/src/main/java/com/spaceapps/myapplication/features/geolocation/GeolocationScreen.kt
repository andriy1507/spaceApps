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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.models.InitState
import com.spaceapps.myapplication.models.LocationUnavailable
import com.spaceapps.myapplication.ui.SPACING_16
import com.spaceapps.myapplication.ui.SPACING_8
import com.spaceapps.myapplication.utils.calculateRectangularCoordinates
import com.spaceapps.myapplication.utils.latitudeString
import com.spaceapps.myapplication.utils.longitudeString
import com.spaceapps.myapplication.views.LoaderIndicator
import dev.chrisbanes.accompanist.insets.statusBarsPadding

@Composable
fun GeolocationScreen() {
    val viewModel = viewModel<GeolocationViewModel>()
    val location = viewModel.lastLocation.observeAsState(null)
    val events = viewModel.events.observeAsState(InitState)
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)
            .statusBarsPadding()
            .padding(horizontal = SPACING_16.dp)
    ) {
        val loc = location.value
        if (loc == null) {
            LoaderIndicator()
        } else {
            GeoCoordinatesCard(loc)
            RectCoordinatesCard(loc)
            if (events.value == LocationUnavailable)
                Row {
                    Icon(
                        modifier = Modifier.padding(end = SPACING_8.dp),
                        imageVector = vectorResource(R.drawable.ic_location_disabled),
                        tint = MaterialTheme.colors.error
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
fun GeoCoordinatesCard(loc: Location) {
    val labelStyle = AmbientTextStyle.current.copy(
        color = MaterialTheme.colors.primary,
        fontWeight = FontWeight.Bold
    )
    Card(
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
            .padding(vertical = SPACING_8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(SPACING_16.dp),
        elevation = 8.dp
    ) {
        Row {

            Column(modifier = Modifier.weight(1f).padding(start = SPACING_16.dp)) {
                Text(
                    text = stringResource(R.string.latitude_label),
                    style = labelStyle,
                    modifier = Modifier.align(Alignment.Start).padding(vertical = SPACING_8.dp)
                )
                Text(
                    text = latitudeString(loc.latitude),
                    modifier = Modifier.align(Alignment.Start).padding(bottom = SPACING_8.dp)
                )
                Text(
                    text = stringResource(R.string.longitude_label),
                    style = labelStyle,
                    modifier = Modifier.align(Alignment.Start).padding(vertical = SPACING_8.dp)
                )
                Text(
                    text = longitudeString(loc.longitude),
                    modifier = Modifier.align(Alignment.Start).padding(bottom = SPACING_8.dp)
                )
            }
            Column(modifier = Modifier.weight(1f).padding(end = SPACING_16.dp)) {
                Text(
                    text = stringResource(R.string.altitude_label),
                    style = labelStyle,
                    modifier = Modifier.align(Alignment.Start).padding(vertical = SPACING_8.dp)
                )
                Text(
                    text = stringResource(R.string.altitude_value, loc.altitude),
                    modifier = Modifier.align(Alignment.Start).padding(bottom = SPACING_8.dp)
                )
                Text(
                    text = stringResource(R.string.accuracy_label),
                    style = labelStyle,
                    modifier = Modifier.align(Alignment.Start).padding(vertical = SPACING_8.dp)
                )
                Text(
                    text = stringResource(R.string.distance_meters, loc.accuracy),
                    modifier = Modifier.align(Alignment.Start).padding(bottom = SPACING_8.dp)
                )
            }
        }
    }
}

@Composable
fun RectCoordinatesCard(loc: Location) {
    Card(
        modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(vertical = SPACING_8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(SPACING_16.dp),
        elevation = SPACING_8.dp
    ) {
        val rect = calculateRectangularCoordinates(loc.longitude, loc.latitude, loc.altitude)
        Column(modifier = Modifier.fillMaxWidth(1f).padding(start = SPACING_16.dp)) {
            Text(
                text = "X: ${rect.x}",
                modifier = Modifier.align(Alignment.Start).padding(vertical = SPACING_8.dp)
            )
            Text(
                text = "Y: ${rect.y}",
                modifier = Modifier.align(Alignment.Start).padding(vertical = SPACING_8.dp)
            )
            Text(
                text = "Z: ${rect.z}",
                modifier = Modifier.align(Alignment.Start).padding(vertical = SPACING_8.dp)
            )
        }
    }
}