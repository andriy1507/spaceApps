package com.spaceapps.myapplication.app.activity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class MenuItem(
    val route: String,
    @DrawableRes val iconId: Int,
    @StringRes val labelId: Int
)
