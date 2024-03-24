package com.example.comprinhas.data

import androidx.room.TypeConverter
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class Converters {
    @TypeConverter
    fun toLocalDateTime(dateSecs: Long): ZonedDateTime {
        return Instant.ofEpochSecond(dateSecs).atZone(ZoneId.systemDefault())
    }

    @TypeConverter
    fun toDateLong(date: ZonedDateTime): Long {
        return date.toEpochSecond()
    }
}