package com.spaceapps.myapplication.app.repositories.locations.results

import com.spaceapps.myapplication.app.models.remote.location.LocationResponse

sealed class UpdateLocationResult {
    class Success(val location: LocationResponse) : UpdateLocationResult()
    class Error(val exception: Exception) : UpdateLocationResult()
}
