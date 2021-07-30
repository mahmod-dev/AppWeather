package com.mahmouddev.appweather.room

import com.mahmouddev.appweather.room.entity.WeatherCity
import com.mahmouddev.appweather.room.entity.WeatherUser

class DatabaseHelperImpl(private val appDatabase: AppDatabase) : DatabaseHelper {
    override suspend fun getAllCities(): List<WeatherCity>? {
       return appDatabase.daoWeather().getAllCities()
    }

    override suspend fun insertCity(weather: WeatherCity): Long {
        return appDatabase.daoWeather().insertCity(weather)
    }

    override suspend fun cityAdded(city: String):WeatherCity? {
        return appDatabase.daoWeather().cityAdded(city)
    }

    override suspend fun currentUserWeather(lat: Double, lng: Double):WeatherUser? {
        return appDatabase.daoWeather().currentUserWeather(lat, lng)
    }

    override suspend fun insertUserWeather(weather: WeatherUser): Long {
        return appDatabase.daoWeather().insertUserWeather(weather)
    }


}