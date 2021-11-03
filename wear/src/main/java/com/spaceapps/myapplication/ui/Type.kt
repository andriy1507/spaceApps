package com.spaceapps.myapplication.ui

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.wear.compose.material.Typography
import com.spaceapps.myapplication.R

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
    display1 = TextStyle(
        fontSize = FONT_50,
        fontWeight = FontWeight.Medium,
        lineHeight = FONT_56
    ),
    display2 = TextStyle(
        fontSize = FONT_40,
        fontWeight = FontWeight.Medium,
        lineHeight = FONT_46
    ),
    display3 = TextStyle(
        fontSize = FONT_30,
        fontWeight = FontWeight.Medium,
        lineHeight = FONT_36
    ),
    title1 = TextStyle(
        fontSize = FONT_24,
        fontWeight = FontWeight.Medium,
        lineHeight = FONT_28
    ),
    title2 = TextStyle(
        fontSize = FONT_20,
        fontWeight = FontWeight.Medium,
        lineHeight = FONT_24
    ),
    title3 = TextStyle(
        fontSize = FONT_16,
        fontWeight = FontWeight.Medium,
        lineHeight = FONT_20
    ),
    body1 = TextStyle(
        fontSize = FONT_16,
        fontWeight = FontWeight.Normal,
        lineHeight = FONT_20
    ),
    body2 = TextStyle(
        fontSize = FONT_14,
        fontWeight = FontWeight.Normal,
        lineHeight = FONT_18
    ),
    button = TextStyle(
        fontSize = FONT_14,
        fontWeight = FontWeight.Bold,
        lineHeight = FONT_18
    ),
    caption1 = TextStyle(
        fontSize = FONT_14,
        fontWeight = FontWeight.Medium,
        lineHeight = FONT_18
    ),
    caption2 = TextStyle(
        fontSize = FONT_12,
        fontWeight = FontWeight.Medium,
        lineHeight = FONT_16
    )
)
