package com.example.photoweatherapp.application

import android.app.Application
import com.example.photoweatherapp.data.local.WeatherDatabase
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * Created by Yasmine on April,2021
 */


class WeatherApplication : Application() {
    @InternalCoroutinesApi
    override fun onCreate() {
        super.onCreate()
        WeatherDatabase.createDatabase(applicationContext)
    }
}