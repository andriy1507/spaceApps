package com.spaceapps.myapplication.features.location.map

import android.location.Location

sealed class GeolocationMapAction {
    object GoToSettings : GeolocationMapAction()
    object GoToLocationsList : GeolocationMapAction()
    data class CameraMoved(val reason: Int) : GeolocationMapAction()
    object FocusClicked : GeolocationMapAction()
    data class AddLocation(val location: Location?) : GeolocationMapAction()
    object TrackLocation : GeolocationMapAction()
}
