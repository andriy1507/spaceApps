package com.spaceapps.myapplication.features.devices

import androidx.annotation.StringRes

sealed class DevicesEvent {

    data class ShowSnackBar(@StringRes val messageId: Int) : DevicesEvent()
}
