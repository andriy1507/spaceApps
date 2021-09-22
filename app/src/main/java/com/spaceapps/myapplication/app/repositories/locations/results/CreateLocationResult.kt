package com.spaceapps.myapplication.app.repositories.locations.results

import com.spaceapps.myapplication.app.models.remote.location.LocationResponse

sealed class CreateLocationResult {
    class Success(val location: LocationResponse) : CreateLocationResult()
    class Error(val exception: Exception) : CreateLocationResult()
}
