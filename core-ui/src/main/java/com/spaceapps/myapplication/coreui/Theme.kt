package com.spaceapps.myapplication.coreui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import com.google.accompanist.insets.ProvideWindowInsets

private val DarkColorPalette = darkColors(
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

private val LightColorPalette = lightColors(
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
        content = { ProvideWindowInsets { content() } }
    )
}
