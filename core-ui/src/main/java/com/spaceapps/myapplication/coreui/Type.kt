package com.spaceapps.myapplication.coreui

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Rubik = FontFamily(
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

val typography = Typography(
    defaultFontFamily = Rubik,
    h1 = TextStyle(
        fontSize = FONT_98,
        letterSpacing = (-1.5).sp,
        fontWeight = FontWeight.Light
    ),
    h2 = TextStyle(
        fontSize = FONT_61,
        letterSpacing = (-0.5).sp,
        fontWeight = FontWeight.Light
    ),
    h3 = TextStyle(
        fontSize = FONT_49,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Normal
    ),
    h4 = TextStyle(
        fontSize = FONT_35,
        letterSpacing = 0.25.sp,
        fontWeight = FontWeight.Normal
    ),
    h5 = TextStyle(
        fontSize = FONT_24,
        letterSpacing = 0.sp,
        fontWeight = FontWeight.Normal
    ),
    h6 = TextStyle(
        fontSize = FONT_20,
        letterSpacing = 0.15.sp,
        fontWeight = FontWeight.Medium
    ),
    subtitle1 = TextStyle(
        fontSize = FONT_16,
        letterSpacing = 0.15.sp,
        fontWeight = FontWeight.Normal
    ),
    subtitle2 = TextStyle(
        fontSize = FONT_14,
        letterSpacing = 0.1.sp,
        fontWeight = FontWeight.Medium
    ),
    body1 = TextStyle(
        fontSize = FONT_16,
        letterSpacing = 0.5.sp,
        fontWeight = FontWeight.Normal
    ),
    body2 = TextStyle(
        fontSize = FONT_14,
        letterSpacing = 0.25.sp,
        fontWeight = FontWeight.Normal
    ),
    button = TextStyle(
        fontSize = FONT_14,
        letterSpacing = 1.25.sp,
        fontWeight = FontWeight.Medium
    ),
    caption = TextStyle(
        fontSize = FONT_12,
        letterSpacing = 0.4.sp,
        fontWeight = FontWeight.Normal
    ),
    overline = TextStyle(
        fontSize = FONT_10,
        letterSpacing = 1.5.sp,
        fontWeight = FontWeight.Normal
    )
)
