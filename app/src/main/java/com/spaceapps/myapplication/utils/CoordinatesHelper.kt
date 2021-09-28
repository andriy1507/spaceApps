package com.spaceapps.myapplication.utils

import android.location.Location
import gov.nasa.worldwind.geom.Angle
import gov.nasa.worldwind.geom.coords.UTMCoord

object CoordinatesHelper {

    fun utmToS63(coord: UTMCoord) {
        // TODO 9/27/2021 Implement conversion to S-63 coordinates
    }

    fun utmToS43(coord: UTMCoord) {
        // TODO 9/27/2021 Implement conversion to S-43 coordinates
    }

    fun locationToUtm(location: Location): UTMCoord {
        val lat = Angle.fromDegreesLatitude(location.latitude)
        val lon = Angle.fromDegreesLongitude(location.longitude)
        return UTMCoord.fromLatLon(lat, lon)
    }
}
