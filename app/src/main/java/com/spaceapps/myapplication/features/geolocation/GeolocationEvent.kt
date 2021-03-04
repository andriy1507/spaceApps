package com.spaceapps.myapplication.features.geolocation

sealed class GeolocationEvent
object LocationUnavailable : GeolocationEvent()
object LocationAvailable : GeolocationEvent()
object InitState : GeolocationEvent()
