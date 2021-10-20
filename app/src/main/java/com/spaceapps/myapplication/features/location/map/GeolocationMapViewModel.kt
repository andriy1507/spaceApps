package com.spaceapps.myapplication.features.location.map

import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.spaceapps.myapplication.app.DEGREES_DMS
import com.spaceapps.myapplication.app.GeolocationGraph
import com.spaceapps.myapplication.app.SYSTEM_GEO
import com.spaceapps.myapplication.app.local.DataStoreManager
import com.spaceapps.myapplication.utils.NavigationDispatcher
import com.spaceapps.myapplication.utils.getStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeolocationMapViewModel @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val navigationDispatcher: NavigationDispatcher,
    savedStateHandle: SavedStateHandle,
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

    val degreesFormat = dataStoreManager.observeDegreesFormat()
        .stateIn(viewModelScope, SharingStarted.Lazily, DEGREES_DMS)
    val coordSystem = dataStoreManager.observeCoordSystem()
        .stateIn(viewModelScope, SharingStarted.Lazily, SYSTEM_GEO)

    @RequiresPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    fun trackLocation() {
        val request = LocationRequest.create().apply {
            fastestInterval = 1500
            interval = 3000
            priority = PRIORITY_HIGH_ACCURACY
        }
        val manager = ContextCompat.getSystemService(
            locationClient.applicationContext,
            LocationManager::class.java
        )
        val enabled = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> manager?.isLocationEnabled
            else -> manager?.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }
        locationClient.requestLocationUpdates(
            request,
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    viewModelScope.launch { location.emit(result.lastLocation) }
                }

                override fun onLocationAvailability(availability: LocationAvailability) {
                }
            },
            Looper.getMainLooper()
        )
    }

    fun onCameraMoved() = viewModelScope.launch { isFocusMode.emit(false) }

    fun onFocusClick() = viewModelScope.launch { isFocusMode.emit(true) }

    fun goToSettings() =
        navigationDispatcher.emit { it.navigate(GeolocationGraph.MapSettings.route) }
}
