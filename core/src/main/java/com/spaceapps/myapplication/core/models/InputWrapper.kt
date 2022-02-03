package com.spaceapps.myapplication.core.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InputWrapper(
    val text: String = "",
    val errorId: Int? = null
) : Parcelable {

    companion object {
        val Empty = InputWrapper()
    }
}
