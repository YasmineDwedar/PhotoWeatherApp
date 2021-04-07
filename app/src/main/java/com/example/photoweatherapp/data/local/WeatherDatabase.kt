package com.example.photoweatherapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.InternalCoroutinesApi

/**
 * Created by Yasmine on April,2021
 */
@Database(entities = [WeatherInfo::class],version = 1)
@TypeConverters(Converters::class)
 abstract class WeatherDatabase:RoomDatabase() {


    abstract fun getWeatherDao():WeatherDAO
    @InternalCoroutinesApi
    companion object{
        //other threads can be notified if the instance changed
        @Volatile
        var  instance:WeatherDatabase? = null


        fun createDatabase(context: Context): WeatherDatabase {
            if (instance == null) {     // 1    2
                kotlinx.coroutines.internal.synchronized(WeatherDatabase::class) {  //
                    if (instance == null) { //2
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            WeatherDatabase::class.java,
                            "weatherinfo.db"
                        ).build()
                    }
                }
            }

            return instance!!

        }

    }
}