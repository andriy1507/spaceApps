package com.spaceapps.myapplication.features.profile

import androidx.annotation.StringRes

sealed class ProfileEvent {

    data class ShowSnackBar(@StringRes val messageId: Int) : ProfileEvent()
}
