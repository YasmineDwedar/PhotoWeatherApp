package com.example.photoweatherapp.data.repository

import com.example.photoweatherapp.data.remote.WeatherAPI
import com.example.photoweatherapp.models.ErrorResponse
import com.example.photoweatherapp.models.WeatherResponse
import com.example.photoweatherapp.util.Constants
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Yasmine on April,2021
 */
class WeatherRepository {

    suspend fun getWeatherData(lat:Double,long: Double): NetworkResponse<WeatherResponse, ErrorResponse> =
        withContext(Dispatchers.IO) {
            return@withContext WeatherAPI.invoke().getWeatherData(lat,long, Constants.API_KEY)
        }
}