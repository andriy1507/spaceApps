package com.spaceapps.myapplication.utils

import android.content.res.Resources
import androidx.compose.runtime.staticCompositionLocalOf

val LocalResources = staticCompositionLocalOf<Resources> { Resources.getSystem() }
