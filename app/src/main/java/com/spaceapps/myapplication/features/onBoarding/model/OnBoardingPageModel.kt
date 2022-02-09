package com.spaceapps.myapplication.features.onBoarding.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class OnBoardingPageModel(
    @DrawableRes val imageRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val textRes: Int
) : Parcelable
