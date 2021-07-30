package com.mahmouddev.appweather.room.dao

import androidx.annotation.NonNull
import androidx.room.*
import com.mahmouddev.appweather.room.entity.WeatherCity
import com.mahmouddev.appweather.room.entity.WeatherUser

@Dao
interface DaoWeather {
    @Query("SELECT * FROM weather_tb")
    @NonNull
    suspend fun getAllCities(): List<WeatherCity>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCity(weather: WeatherCity): Long

    @Query("SELECT * FROM weather_tb WHERE city = :city")
    suspend fun cityAdded(city:String):WeatherCity?

    @Query("SELECT * FROM current_user_tb WHERE lat = :lat AND lng = :lng")
    suspend fun currentUserWeather(lat:Double,lng:Double):WeatherUser?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserWeather(weather: WeatherUser): Long
}