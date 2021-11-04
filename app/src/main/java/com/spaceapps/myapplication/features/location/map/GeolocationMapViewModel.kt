package com.spaceapps.myapplication.features.location.map

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.model.cameraPosition
import com.google.maps.android.ktx.model.markerOptions
import com.spaceapps.myapplication.R
import com.spaceapps.myapplication.app.Screens.*
import com.spaceapps.myapplication.core.DEFAULT_MAP_ZOOM
import com.spaceapps.myapplication.core.DEGREES_DMS
import com.spaceapps.myapplication.core.SYSTEM_GEO
import com.spaceapps.myapplication.core.local.DataStoreManager
import com.spaceapps.myapplication.utils.NavigationDispatcher
import com.spaceapps.myapplication.core.utils.getStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeolocationMapViewModel @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val navigationDispatcher: NavigationDispatcher,
    private val savedStateHandle: SavedStateHandle,
    dataStoreManager: DataStoreManager
) : ViewModel() {

    val isFocusMode = savedStateHandle.getStateFlow(
        scope = viewModelScope,
        key = "isFocusMode",
        initialValue = true
    )
    val location = savedStateHandle.getStateFlow<Location?>(
        scope = viewModelScope,
        key = "location",
        initialValue = null
    )
    private val _events = MutableSharedFlow<GeolocationMapEvents>()
    val events: SharedFlow<GeolocationMapEvents>
        get() = _events.asSharedFlow()

    val degreesFormat = dataStoreManager.observeDegreesFormat()
        .stateIn(viewModelScope, SharingStarted.Lazily, DEGREES_DMS)
    val coordSystem = dataStoreManager.observeCoordSystem()
        .stateIn(viewModelScope, SharingStarted.Lazily, SYSTEM_GEO)

    @SuppressLint("MissingPermission")
    fun trackLocation() {
        val request = LocationRequest.create().apply {
            fastestInterval = 1500
            interval = 3000
            priority = PRIORITY_HIGH_ACCURACY
        }
        locationClient.requestLocationUpdates(
            request,
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    viewModelScope.launch { location.emit(result.lastLocation) }
                    updateMarker(result.lastLocation)
                    updateCamera(result.lastLocation)
                }

                override fun onLocationAvailability(availability: LocationAvailability) = Unit
            },
            Looper.getMainLooper()
        )
    }

    private fun updateCamera(lastLocation: Location) = viewModelScope.launch {
        if (!isFocusMode.value) return@launch
        val update = CameraUpdateFactory.newCameraPosition(
            cameraPosition {
                target(LatLng(lastLocation.latitude, lastLocation.longitude))
                zoom(DEFAULT_MAP_ZOOM)
            }
        )
        _events.emit(GeolocationMapEvents.UpdateCamera(update))
    }

    private fun updateMarker(lastLocation: Location) = viewModelScope.launch {
        val latLng = LatLng(lastLocation.latitude, lastLocation.longitude)
        _events.emit(GeolocationMapEvents.AddMarker(markerOptions { position(latLng) }))
    }

    fun onCameraMoved(reason: Int) =
        viewModelScope.launch { if (reason == REASON_GESTURE) isFocusMode.emit(false) }

    fun onFocusClick() = viewModelScope.launch {
        isFocusMode.emit(true)
        location.value?.let { updateCamera(it) }
    }

    fun goToSettings() =
        navigationDispatcher.emit { it.navigate(MapSettings.route) }

    fun goLocationsList() =
        navigationDispatcher.emit { it.navigate(LocationsList.route) }

    fun addLocation(location: Location?) = viewModelScope.launch {
        _events.emit(GeolocationMapEvents.ShowSnackBar(R.string.not_implemented_yet))
    }
}
