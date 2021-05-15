package com.spaceapps.myapplication.features.geolocation

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.GoogleMap
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GeolocationViewModel @Inject constructor(
    private val locationClient: FusedLocationProviderClient,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val lastLocation = savedStateHandle.getLiveData<Location>("location")
    val events = MutableLiveData<GeolocationEvent>()
    val isMapTracking = MutableLiveData(true)
    val mapType = MutableLiveData(GoogleMap.MAP_TYPE_NORMAL)

    fun setMapTracking(tracking: Boolean) = isMapTracking.postValue(tracking)

    fun setMapType(type: Int) = mapType.postValue(type)

    @SuppressLint("MissingPermission")
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
        val enabled = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) manager?.isLocationEnabled
        else manager?.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (enabled != true) events.postValue(LocationUnavailable)
        locationClient.requestLocationUpdates(
            request,
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    lastLocation.postValue(result.lastLocation)
                }

                override fun onLocationAvailability(availability: LocationAvailability) {
                    if (availability.isLocationAvailable) {
                        events.postValue(LocationAvailable)
                    } else {
                        events.postValue(LocationUnavailable)
                    }
                }
            },
            Looper.getMainLooper()
        )
    }
}
