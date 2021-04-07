package com.example.photoweatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Created by Yasmine on April,2021
 */
@Dao
interface WeatherDAO {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherInfo(weather: WeatherInfo)

    @Query("SELECT * FROM weather")
    fun getAllWeatherInfo(): Flow<List<WeatherInfo>>


}