package com.spaceapps.myapplication.core.repositories.locations

import androidx.paging.Pager
import com.spaceapps.myapplication.core.models.local.LocationEntity
import com.spaceapps.myapplication.core.models.remote.locations.LocationRequest
import com.spaceapps.myapplication.core.repositories.locations.results.CreateLocationResult
import com.spaceapps.myapplication.core.repositories.locations.results.DeleteLocationResult
import com.spaceapps.myapplication.core.repositories.locations.results.UpdateLocationResult

interface LocationsRepository {

    fun getLocationsByName(name: String?): Pager<Int, LocationEntity>

    suspend fun deleteLocation(id: Int): DeleteLocationResult

    suspend fun updateLocation(id: Int, request: LocationRequest): UpdateLocationResult

    suspend fun createLocation(request: LocationRequest): CreateLocationResult
}
