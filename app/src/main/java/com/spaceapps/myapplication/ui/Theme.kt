package com.spaceapps.myapplication.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets

private val DarkColorPalette = darkColors(
    primary = pinkDark,
    primaryVariant = pinkDarkVariant,
    secondary = orangeDark,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
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
    onPrimary = Color.White,
    onSecondary = Color.White,
    background = background,
    surface = surface,
    onSurface = onSurface,
    onBackground = onSurface
)

@Composable
fun MyApplicationTheme(
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
        content = { ProvideWindowInsets(content = content) }
    )
}
