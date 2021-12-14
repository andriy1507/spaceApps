package com.spaceapps.myapplication.features.location.map

import androidx.annotation.StringRes
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.model.MarkerOptions

sealed class GeolocationMapEvent {
    data class ShowSnackBar(@StringRes val messageId: Int) : GeolocationMapEvent()
    data class AddMarker(val options: MarkerOptions) : GeolocationMapEvent()
    data class UpdateCamera(val update: CameraUpdate) : GeolocationMapEvent()
}
