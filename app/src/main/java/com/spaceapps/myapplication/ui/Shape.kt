package com.spaceapps.myapplication.ui

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes

val shapes = Shapes(
    small = CircleShape,
    medium = RoundedCornerShape(SPACING_8),
    large = RoundedCornerShape(SPACING_16)
)
