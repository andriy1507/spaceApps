package com.spaceapps.myapplication.features.geolocation

import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.spaceapps.myapplication.models.GeolocationEvent
import com.spaceapps.myapplication.models.LocationAvailable
import com.spaceapps.myapplication.models.LocationUnavailable
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GeolocationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val locationClient: FusedLocationProviderClient
) : ViewModel() {

    val lastLocation = MutableLiveData<Location>()
    val events = MutableLiveData<GeolocationEvent>()

    @RequiresPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    fun trackLocation() {
        val request = LocationRequest().apply {
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
        locationClient.requestLocationUpdates(request, object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                lastLocation.postValue(result.lastLocation)
            }

            override fun onLocationAvailability(availability: LocationAvailability) {
                if (availability.isLocationAvailable) events.postValue(LocationAvailable)
                else events.postValue(LocationUnavailable)
            }
        }, Looper.getMainLooper())
    }
}