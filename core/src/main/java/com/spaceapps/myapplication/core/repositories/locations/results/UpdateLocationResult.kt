package com.spaceapps.myapplication.core.repositories.locations.results

import com.spaceapps.myapplication.core.models.remote.locations.LocationResponse

sealed class UpdateLocationResult {
    class Success(val location: LocationResponse) : UpdateLocationResult()
    class Error(val exception: Exception) : UpdateLocationResult()
}
