package com.spaceapps.myapplication.features.location.map

import android.location.Location
import android.os.Parcelable
import com.spaceapps.myapplication.core.DEGREES_DMS
import com.spaceapps.myapplication.core.SYSTEM_GEO
import kotlinx.parcelize.Parcelize

@Parcelize
data class GeolocationMapViewState(
    val location: Location? = null,
    val isFocusMode: Boolean = true,
    val degreesFormat: String = DEGREES_DMS,
    val coordSystem: String = SYSTEM_GEO
) : Parcelable {
    companion object {
        val Empty = GeolocationMapViewState()
    }
}
