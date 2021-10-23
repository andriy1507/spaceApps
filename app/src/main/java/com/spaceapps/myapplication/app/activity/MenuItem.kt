package com.spaceapps.myapplication.app.activity

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val route: String,
    val icon: ImageVector,
    @StringRes val labelId: Int
)
