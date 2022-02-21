package com.spaceapps.myapplication.features.location.map

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE
import com.spaceapps.navigation.Screens.*
import com.spaceapps.myapplication.core.DEGREES_DMS
import com.spaceapps.myapplication.core.SYSTEM_GEO
import com.spaceapps.myapplication.core.local.DataStoreManager
import com.spaceapps.myapplication.core.utils.getStateFlow
import com.spaceapps.myapplication.features.location.map.GeolocationMapAction.*
import com.spaceapps.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeolocationMapViewModel @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    private val navigator: Navigator,
    savedStateHandle: SavedStateHandle,
    dataStoreManager: DataStoreManager
) : ViewModel() {

    private val isFocusMode = savedStateHandle.getStateFlow(
        scope = viewModelScope,
        key = "isFocusMode",
        initialValue = true
    )
    private val location = savedStateHandle.getStateFlow<Location?>(
        scope = viewModelScope,
        key = "location",
        initialValue = null
    )
    private val _events = MutableSharedFlow<GeolocationMapEvent>()

    private val pendingActions = MutableSharedFlow<GeolocationMapAction>()
    val events: SharedFlow<GeolocationMapEvent>
        get() = _events.asSharedFlow()

    private val degreesFormat = dataStoreManager.observeDegreesFormat()
        .stateIn(viewModelScope, SharingStarted.Lazily, DEGREES_DMS)
    private val coordSystem = dataStoreManager.observeCoordSystem()
        .stateIn(viewModelScope, SharingStarted.Lazily, SYSTEM_GEO)

    val state = combine(
        location,
        isFocusMode,
        degreesFormat,
        coordSystem
    ) { location, isFocusMode, degreesFormat, coordSystem ->
        GeolocationMapViewState(location, isFocusMode, degreesFormat, coordSystem)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = GeolocationMapViewState.Empty
    )

    init {
        collectActions()
    }

    private fun collectActions() = viewModelScope.launch {
        pendingActions.collect { action ->
            when (action) {
                is AddLocation -> addLocation(action.location)
                is CameraMoved -> onCameraMoved(action.reason)
                is FocusClicked -> onFocusClick()
                is GoToLocationsList -> goLocationsList()
                is GoToSettings -> goToSettings()
                is TrackLocation -> trackLocation()
            }
        }
    }

    fun submitAction(action: GeolocationMapAction) = viewModelScope.launch {
        pendingActions.emit(action)
    }

    @SuppressLint("MissingPermission")
    private fun trackLocation() {
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
                }

                override fun onLocationAvailability(availability: LocationAvailability) = Unit
            },
            Looper.getMainLooper()
        )
    }

    private fun onCameraMoved(reason: Int) =
        viewModelScope.launch { if (reason == REASON_GESTURE) isFocusMode.emit(false) }

    private fun onFocusClick() = viewModelScope.launch {
        isFocusMode.emit(true)
    }

    private fun goToSettings() =
        navigator.emit { it.navigate(MapSettings.route) }

    private fun goLocationsList() =
        navigator.emit { it.navigate(LocationsList.route) }

    private fun addLocation(location: Location?) = viewModelScope.launch {
        navigator.emit { it.navigate(SaveLocation.route) }
    }
}
