package com.spaceapps.myapplication.utils

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.ScalingLazyListScope

fun <T> ScalingLazyListScope.items(items: List<T>, content: @Composable (T) -> Unit) {
    items(items.size) {
        content(items[it])
    }
}
