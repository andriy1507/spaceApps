package com.spaceapps.myapplication.local

import androidx.room.TypeConverter
import com.spaceapps.myapplication.utils.MoshiConverters
import org.joda.time.LocalDateTime

class RoomTypeConverters {

    private val moshiConverter = MoshiConverters()

    @TypeConverter
    fun localDateTimeToString(dateTime: LocalDateTime): String {
        return moshiConverter.dateTimeToJson(dateTime)
    }

    @TypeConverter
    fun stringToLocalDateTime(string: String): LocalDateTime {
        return moshiConverter.jsonToDateTime(string)
    }

}