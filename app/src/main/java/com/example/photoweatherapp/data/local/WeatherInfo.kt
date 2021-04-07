package com.example.photoweatherapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.photoweatherapp.models.Main
import com.example.photoweatherapp.models.Sys
import com.example.photoweatherapp.models.WeatherResponse
import com.example.photoweatherapp.models.Wind

/**
 * Created by Yasmine on April,2021
 */
@Entity(tableName = "weather")
data class WeatherInfo(
    val feels_like: Double,
    val temp: Double,
    val name: String,
    val sys: Sys,
    val wind: Wind,
    var img:String,
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
)
