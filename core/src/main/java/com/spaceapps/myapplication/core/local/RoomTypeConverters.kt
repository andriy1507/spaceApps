package com.spaceapps.myapplication.core.local

import androidx.room.TypeConverter
import com.spaceapps.myapplication.core.utils.MoshiConverters
import java.time.LocalDateTime

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
