package com.spaceapps.myapplication.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection

@Composable
operator fun PaddingValues.minus(other: PaddingValues): PaddingValues {
    val top = other.calculateTopPadding()
    val bottom = other.calculateBottomPadding()
    val end = other.calculateEndPadding(LocalLayoutDirection.current)
    val start = other.calculateStartPadding(LocalLayoutDirection.current)
    return PaddingValues(
        top = calculateTopPadding() - top,
        bottom = bottom + calculateBottomPadding() - bottom,
        end = calculateEndPadding(LocalLayoutDirection.current) - end,
        start = calculateStartPadding(LocalLayoutDirection.current) - start
    )
}

@Composable
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    val top = other.calculateTopPadding()
    val bottom = other.calculateBottomPadding()
    val end = other.calculateEndPadding(LocalLayoutDirection.current)
    val start = other.calculateStartPadding(LocalLayoutDirection.current)
    return PaddingValues(
        top = top + calculateTopPadding(),
        bottom = bottom + calculateBottomPadding(),
        end = end + calculateEndPadding(LocalLayoutDirection.current),
        start = start + calculateStartPadding(LocalLayoutDirection.current)
    )
}
