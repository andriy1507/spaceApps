package com.spaceapps.myapplication.core.repositories.locations.results

import com.spaceapps.myapplication.core.models.remote.locations.LocationResponse

sealed class CreateLocationResult {
    class Success(val location: LocationResponse) : CreateLocationResult()
    class Error(val exception: Exception) : CreateLocationResult()
}
