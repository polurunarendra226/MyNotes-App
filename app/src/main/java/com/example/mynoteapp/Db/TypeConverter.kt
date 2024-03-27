package com.example.mynoteapp.Db

import androidx.room.TypeConverter
import java.util.Date

class TypeConverter {
@TypeConverter
    fun fromDate(date:Long):Date{
        return Date(date)

    }@TypeConverter
    fun toDate(date:Date):Long{
        return date.time
    }
}