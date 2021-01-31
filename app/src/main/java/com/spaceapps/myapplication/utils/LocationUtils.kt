package com.spaceapps.myapplication.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.spaceapps.myapplication.R
import kotlin.math.abs

@Composable
fun longitudeString(lon: Double): String {
    val suffix = stringResource(id = if (lon >= 0) R.string.east else R.string.west)
    return stringResource(id = R.string.degree_value, abs(lon)) + ' ' + suffix
}

@Composable
fun latitudeString(lat: Double): String {
    val suffix = stringResource(id = if (lat >= 0) R.string.north else R.string.south)
    return stringResource(id = R.string.degree_value, abs(lat)) + ' ' + suffix
}
