package com.example.photoweatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by Yasmine on April,2021
 */
@Dao
interface WeatherDAO {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherInfo(weather: List<WeatherInfo>)

    @Query("SELECT * FROM weather")
    suspend fun getAllWeatherInfo(): List<WeatherInfo>

 
}