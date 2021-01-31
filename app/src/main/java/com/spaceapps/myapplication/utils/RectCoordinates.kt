package com.spaceapps.myapplication.utils

import android.location.Location

data class RectCoordinates(
    val x: Double,
    val y: Double,
    val z: Double
)

fun calculateRectangularCoordinates(
    longitude: Double,
    latitude: Double,
    altitude: Double
): RectCoordinates {
    val loc = Location("").apply {
        this.latitude = latitude
        this.longitude = longitude
    }
    val x = loc.distanceTo(Location(loc).apply { this.latitude = 0.0 })
    val y = loc.distanceTo(Location(loc).apply { this.longitude = 0.0 })
    return RectCoordinates(x.toDouble(), y.toDouble(), altitude)
}
