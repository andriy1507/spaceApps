package com.spaceapps.myapplication.utils

import retrofit2.Response

val Response<*>?.errorMessage: String
    get() = this?.errorBody()?.string().orEmpty()