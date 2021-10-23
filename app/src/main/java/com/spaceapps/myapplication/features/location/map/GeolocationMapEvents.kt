package com.spaceapps.myapplication.features.location.map

import androidx.annotation.StringRes
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.model.MarkerOptions

sealed class GeolocationMapEvents {
    data class ShowSnackBar(@StringRes val messageId: Int) : GeolocationMapEvents()
    data class AddMarker(val options: MarkerOptions) : GeolocationMapEvents()
    data class UpdateCamera(val update: CameraUpdate) : GeolocationMapEvents()
}
