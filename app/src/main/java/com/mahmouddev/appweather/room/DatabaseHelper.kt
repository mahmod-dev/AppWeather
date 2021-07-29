package com.mahmouddev.appweather.room

import com.mahmouddev.appweather.room.entity.Weather

interface DatabaseHelper {
    suspend fun getAllCities(): List<Weather>



}