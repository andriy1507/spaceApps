package com.spaceapps.myapplication.app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InputWrapper(
    val text: String = "",
    val errorId: Int? = null
) : Parcelable
