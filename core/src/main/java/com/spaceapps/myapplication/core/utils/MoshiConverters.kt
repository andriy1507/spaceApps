package com.spaceapps.myapplication.core.utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MoshiConverters {

    @FromJson
    fun jsonToDateTime(string: String): LocalDateTime {
        return LocalDateTime.parse(string, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    @ToJson
    fun dateTimeToJson(localDateTime: LocalDateTime): String {
        return localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
}
