package com.mahmouddev.appweather.room

import com.mahmouddev.appweather.room.entity.Weather

class DatabaseHelperImpl(private val appDatabase: AppDatabase) : DatabaseHelper {
    override suspend fun getAllCities(): List<Weather> {
       return appDatabase.daoWeather().getAllCities()
    }


}