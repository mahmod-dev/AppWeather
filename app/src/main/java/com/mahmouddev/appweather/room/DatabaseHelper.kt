package com.mahmouddev.appweather.room

import com.mahmouddev.appweather.room.entity.WeatherCity
import com.mahmouddev.appweather.room.entity.WeatherUser

interface DatabaseHelper {
    suspend fun getAllCities(): List<WeatherCity>?

    suspend fun insertCity(weather: WeatherCity): Long

    suspend fun cityAdded(city:String):WeatherCity?

    suspend fun currentUserWeather(lat:Double,lng:Double): WeatherUser?

    suspend fun insertUserWeather(weather: WeatherUser): Long

}