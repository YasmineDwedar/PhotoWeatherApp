package com.example.photoweatherapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.photoweatherapp.models.WeatherResponse

/**
 * Created by Yasmine on April,2021
 */
@Entity(tableName = "weather")
data class WeatherInfo(
    var weatherResponse: WeatherResponse,
    var img:String,
    @PrimaryKey
    var id:Int
)
