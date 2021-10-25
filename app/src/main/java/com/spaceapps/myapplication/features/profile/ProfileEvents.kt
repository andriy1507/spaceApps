package com.spaceapps.myapplication.features.profile

import androidx.annotation.StringRes

sealed class ProfileEvents {

    data class ShowSnackBar(@StringRes val messageId: Int) : ProfileEvents()
}
