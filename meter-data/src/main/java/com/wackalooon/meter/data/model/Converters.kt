package com.wackalooon.meter.data.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.wackalooon.meter.domain.model.Type

class Converters {

    @TypeConverter
    fun convertTypeToString(type: Type): String {
        return Gson().toJson(type)
    }

    @TypeConverter
    fun convertStringToType(type: String): Type {
        return Gson().fromJson(type, Type::class.java)
    }
}