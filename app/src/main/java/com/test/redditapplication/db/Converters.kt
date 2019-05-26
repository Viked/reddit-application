package com.test.redditapplication.db

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun timestampToDate(time: Long) = Date(time)

    @TypeConverter
    fun dateToTimestamp(date: Date) = date.time

}