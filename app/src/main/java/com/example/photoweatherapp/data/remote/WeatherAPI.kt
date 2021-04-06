package com.example.photoweatherapp.data.remote

import androidx.lifecycle.LiveData
import com.example.photoweatherapp.models.ErrorResponse
import com.example.photoweatherapp.models.WeatherResponse
import com.example.photoweatherapp.util.Constants
import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Yasmine on April,2021
 */
interface WeatherAPI {
    @GET(Constants.Current_Weather)
  suspend  fun getWeatherData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") api:String
    ): NetworkResponse<WeatherResponse, ErrorResponse>


    companion object {
        fun getMoshi(): Moshi {
            return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        }

        operator fun invoke(): WeatherAPI {
            return Retrofit.Builder()
                .addCallAdapterFactory(NetworkResponseAdapterFactory())
                .addConverterFactory(MoshiConverterFactory.create(getMoshi()))
                .baseUrl(Constants.BASE_URL)
                .build()
                .create(WeatherAPI::class.java)
        }

    }
}