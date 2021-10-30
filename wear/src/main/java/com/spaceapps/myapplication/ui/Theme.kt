package com.spaceapps.myapplication.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Shapes
import androidx.wear.compose.material.Typography

private val DarkColorPalette = Colors(
    primary = pinkDark,
    primaryVariant = pinkDarkVariant,
    secondary = orangeDark,
    onPrimary = onPrimaryColor,
    onSecondary = onSecondaryColor,
    background = backgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    onBackground = onSurfaceDark
)

private val LightColorPalette = Colors(
    primary = pink,
    primaryVariant = pinkVariant,
    secondary = orange,
    secondaryVariant = orangeVariant,
    onPrimary = onPrimaryColor,
    onSecondary = onSecondaryColor,
    background = background,
    surface = surface,
    onSurface = onSurface,
    onBackground = onSurface
)

@Composable
fun SpaceAppsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    lightColors: Colors = LightColorPalette,
    darkColors: Colors = DarkColorPalette,
    type: Typography = typography,
    shape: Shapes = shapes,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) darkColors else lightColors

    MaterialTheme(
        colors = colors,
        typography = type,
        shapes = shape,
        content = content
    )
}
