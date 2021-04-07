package com.example.photoweatherapp.data.local

import androidx.room.TypeConverter
import com.example.photoweatherapp.models.Sys
import com.example.photoweatherapp.models.Wind

/**
 * Created by Yasmine on April,2021
 */


//Room doesnt understand except primitives source is a custum object by us
class Converters {
    @TypeConverter
    fun fromWind(wind: Wind): Double {
        return wind.speed
    }

    @TypeConverter
    fun toWind(speed: Double): Wind {
        return Wind(speed)
    }    @TypeConverter
    fun fromSys(sys: Sys): String {
        return sys.country
    }

    @TypeConverter
    fun toSys(string: String): Sys {
        return Sys(string)
    }
}