package com.spaceapps.myapplication.features.geolocation

import android.location.Location
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.insets.systemBarsPadding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.spaceapps.myapplication.ui.ACTION_BAR_SIZE
import com.spaceapps.myapplication.ui.SPACING_1
import com.spaceapps.myapplication.ui.SPACING_16
import com.spaceapps.myapplication.ui.SPACING_8
import com.spaceapps.myapplication.ui.views.GoogleMap
import com.spaceapps.myapplication.utils.LocalResources

const val MOCK_PROVIDER = "MOCK_PROVIDER"
typealias OnMapTrackingChange = (Boolean) -> Unit

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GeolocationScreen(
    fragmentManager: FragmentManager,
    location: Location,
    isMapTracking: Boolean,
    onMapTrackingChange: OnMapTrackingChange
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .statusBarsPadding()
            .padding(horizontal = SPACING_16.dp)
    ) {
        if (location.provider == MOCK_PROVIDER) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            MapPreview(
                fragmentManager = fragmentManager,
                location = location,
                isMapTracking = isMapTracking,
                onMapTrackingChange = onMapTrackingChange
            )
            AnimatedVisibility(visible = !isMapTracking, enter = fadeIn(), exit = fadeOut()) {
                Button(onClick = { onMapTrackingChange(true) }) {}
            }
        }
    }
}

@Composable
fun MapPreview(
    fragmentManager: FragmentManager,
    location: Location,
    isMapTracking: Boolean,
    onMapTrackingChange: OnMapTrackingChange
) {
    val density = LocalResources.current.displayMetrics.density
    val circleStrokeColor = MaterialTheme.colors.primary.copy(alpha = .85f).toArgb()
    val circleFillColor = MaterialTheme.colors.primary.copy(alpha = .25f).toArgb()
    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(bottom = (ACTION_BAR_SIZE + SPACING_8).dp)
            .clip(RoundedCornerShape(size = SPACING_16.dp))
            .background(color = MaterialTheme.colors.surface),
        manager = fragmentManager,
        onMapLoaded = {
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
