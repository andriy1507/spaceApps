package com.spaceapps.myapplication.features.home

import androidx.annotation.StringRes

data class HomeMenuItem(
    @StringRes val titleRes: Int,
    val route: String
)
