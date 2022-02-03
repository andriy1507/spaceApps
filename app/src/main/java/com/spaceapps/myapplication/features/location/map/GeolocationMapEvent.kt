package com.spaceapps.myapplication.features.location.map

import androidx.annotation.StringRes

sealed class GeolocationMapEvent {
    data class ShowSnackBar(@StringRes val messageId: Int) : GeolocationMapEvent()
}
