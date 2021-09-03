package com.spaceapps.myapplication.features.geolocation

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.ui.OnClick
import com.spaceapps.myapplication.ui.views.GoogleMap
import timber.log.Timber

@SuppressLint("MissingPermission")
@Composable
fun GeolocationScreen(vm: GeolocationViewModel) {
    val locationRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (permissions.all { it.value }) {
                vm.trackLocation()
            } else {
                Timber.e("Permission not granted")
            }
        }
    )
    LaunchedEffect(Unit) {
        locationRequest.launch(arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION))
    }
    Scaffold(
        floatingActionButton = { MapTrackingFab(onClick = { }) },
        content = { MapPreview() }
    )
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
fun MapPreview() {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        onMapLoaded = {

        }
    )
}
