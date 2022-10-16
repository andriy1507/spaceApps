package com.spaceapps.myapplication.coreui.material3

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.spaceapps.myapplication.core_ui.R
import com.spaceapps.myapplication.coreui.*

val RubikFontFamily = FontFamily(
    Font(resId = R.font.rubik_light, weight = FontWeight.W300),
    Font(resId = R.font.rubik_light_italic, weight = FontWeight.W300, style = FontStyle.Italic),
    Font(resId = R.font.rubik_regular, weight = FontWeight.W400),
    Font(resId = R.font.rubik_italic, weight = FontWeight.W400, style = FontStyle.Italic),
    Font(resId = R.font.rubik_medium, weight = FontWeight.W500),
    Font(resId = R.font.rubik_medium_italic, weight = FontWeight.W500, style = FontStyle.Italic),
    Font(resId = R.font.rubik_semibold, weight = FontWeight.W600),
    Font(resId = R.font.rubik_semibold_italic, weight = FontWeight.W600, style = FontStyle.Italic),
    Font(resId = R.font.rubik_bold, weight = FontWeight.W700),
    Font(resId = R.font.rubik_bold_italic, weight = FontWeight.W700, style = FontStyle.Italic),
    Font(resId = R.font.rubik_extrabold, weight = FontWeight.W800),
    Font(resId = R.font.rubik_extrabold_italic, weight = FontWeight.W800, style = FontStyle.Italic),
    Font(resId = R.font.rubik_black, weight = FontWeight.W900),
    Font(resId = R.font.rubik_black_italic, weight = FontWeight.W900, style = FontStyle.Italic)
)

val RubikTypography = Typography(
    displayLarge = TextStyle(
        fontSize = FONT_57,
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Normal
    ),
    displayMedium = TextStyle(
        fontSize = FONT_45,
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Normal
    ),
    displaySmall = TextStyle(
        fontSize = FONT_36,
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Normal
    ),
    headlineLarge = TextStyle(
        fontSize = FONT_32,
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Normal
    ),
    headlineMedium = TextStyle(
        fontSize = FONT_28,
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Normal
    ),
    headlineSmall = TextStyle(
        fontSize = FONT_24,
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Normal
    ),
    titleLarge = TextStyle(
        fontSize = FONT_22,
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Normal
    ),
    titleMedium = TextStyle(
        fontSize = FONT_16,
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Medium
    ),
    titleSmall = TextStyle(
        fontSize = FONT_14,
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Medium
    ),
    bodyLarge = TextStyle(
        fontSize = FONT_16,
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Normal
    ),
    bodyMedium = TextStyle(
        fontSize = FONT_14,
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Normal
    ),
    bodySmall = TextStyle(
        fontSize = FONT_12,
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Normal
    ),
    labelLarge = TextStyle(
        fontSize = FONT_14,
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Medium
    ),
    labelMedium = TextStyle(
        fontSize = FONT_12,
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Medium
    ),
    labelSmall = TextStyle(
        fontSize = FONT_11,
        fontFamily = RubikFontFamily,
        fontWeight = FontWeight.Medium
    )
)
