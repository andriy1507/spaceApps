package com.spaceapps.myapplication.features.geolocation

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.spaceapps.myapplication.ui.SpaceAppsTheme
import com.spaceapps.myapplication.utils.ComposableFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GeolocationFragment : ComposableFragment() {

    private val permissionRequest = registerForActivityResult(RequestPermission()) { granted ->
        if (granted) tryTrackingLocationOrRequestPermission()
    }

    private val viewModel by viewModels<GeolocationViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tryTrackingLocationOrRequestPermission()
    }

    @Composable
    override fun Content() = SpaceAppsTheme { GeolocationScreen() }

    private fun tryTrackingLocationOrRequestPermission() {
        if (requireContext().checkSelfPermission(ACCESS_FINE_LOCATION) == PERMISSION_GRANTED)
            viewModel.trackLocation()
        else requestPermission()
    }

    private fun requestPermission() = permissionRequest.launch(ACCESS_FINE_LOCATION)
}
