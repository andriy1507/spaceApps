package com.spaceapps.myapplication.utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

class MoshiConverters {

    private val formatter = DateTimeFormat.forPattern("YYYY-MM-dd'T'HH:mm:ss.SSS'Z'")

    @FromJson
    fun jsonToDateTime(string: String): LocalDateTime {
        return LocalDateTime.parse(string, formatter)
    }

    @ToJson
    fun dateTimeToJson(localDateTime: LocalDateTime): String {
        return localDateTime.toString(formatter)
    }
}