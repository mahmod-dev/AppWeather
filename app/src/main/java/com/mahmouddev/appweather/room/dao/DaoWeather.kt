package com.mahmouddev.appweather.room.dao

import androidx.room.*
import com.mahmouddev.appweather.room.entity.Weather

@Dao
interface DaoWeather {
    @Query("SELECT * FROM weather_tb")
    suspend fun getAllCities(): List<Weather>



}