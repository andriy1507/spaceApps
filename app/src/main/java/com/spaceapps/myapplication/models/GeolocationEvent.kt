package com.spaceapps.myapplication.models

sealed class GeolocationEvent
object LocationUnavailable : GeolocationEvent()
object LocationAvailable : GeolocationEvent()
object InitState : GeolocationEvent()
