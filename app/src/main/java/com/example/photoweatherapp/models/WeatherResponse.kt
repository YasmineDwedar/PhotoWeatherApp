package com.example.photoweatherapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize

data class WeatherResponse(
    val base: String,
    val clouds: Clouds?,
    val cod: Int,
    val coord: Coord?,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
):Parcelable


@Parcelize
data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
):Parcelable

@Parcelize
data class Wind(
    val speed: Double
):Parcelable

@Parcelize
data class Sys(
    val country: String

):Parcelable

@Parcelize
data class Main(
    val feels_like: Double,
    val temp: Double,

):Parcelable

@Parcelize
data class Coord(
    val lat: Double,
    val lon: Double
):Parcelable

@Parcelize
data class Clouds(
    val all: Int
):Parcelable