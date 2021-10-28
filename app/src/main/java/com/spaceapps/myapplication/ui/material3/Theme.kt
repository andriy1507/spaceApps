package com.spaceapps.myapplication.ui.material3

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.BuildCompat
import com.google.accompanist.insets.ProvideWindowInsets

@Composable
fun SpaceAppsMaterial3Theme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    lightColorScheme: ColorScheme = LightColorScheme,
    darkColorScheme: ColorScheme = DarkColorScheme,
    typography: Typography = RubikTypography,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        BuildCompat.isAtLeastS() && isDarkTheme -> dynamicDarkColorScheme(LocalContext.current)
        BuildCompat.isAtLeastS() && !isDarkTheme -> dynamicLightColorScheme(LocalContext.current)
        isDarkTheme -> darkColorScheme
        else -> lightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = { ProvideWindowInsets(content = content) }
    )
}
