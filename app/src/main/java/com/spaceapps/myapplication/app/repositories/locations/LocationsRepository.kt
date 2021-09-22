package com.spaceapps.myapplication.app.repositories.locations

import androidx.paging.Pager
import com.spaceapps.myapplication.app.models.local.LocationEntity
import com.spaceapps.myapplication.app.models.remote.location.LocationRequest
import com.spaceapps.myapplication.app.repositories.locations.results.CreateLocationResult
import com.spaceapps.myapplication.app.repositories.locations.results.DeleteLocationResult
import com.spaceapps.myapplication.app.repositories.locations.results.UpdateLocationResult

interface LocationsRepository {

    fun getLocationsByName(name: String): Pager<Int, LocationEntity>

    suspend fun deleteLocation(id: Int): DeleteLocationResult

    suspend fun updateLocation(id: Int, request: LocationRequest): UpdateLocationResult

    suspend fun createLocation(request: LocationRequest): CreateLocationResult
}
