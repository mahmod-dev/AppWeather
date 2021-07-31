package com.mahmouddev.appweather.repository

import com.mahmouddev.appweather.retrofit.ApiHelper
import com.mahmouddev.appweather.room.DatabaseHelper
import com.mahmouddev.appweather.room.entity.WeatherCity
import com.mahmouddev.appweather.room.entity.WeatherUser

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val apiHelper: ApiHelper?,
    private val dbHelper: DatabaseHelper?
) {

    suspend fun searchByCity(cityName: String) = apiHelper?.searchByCity(cityName)

    suspend fun dailyWeather(cityName: String) = apiHelper?.dailyWeather(cityName)

    suspend fun currentUserLocation(lat: Double, lon: Double) =
        apiHelper?.currentUserLocation(lat, lon)

    suspend fun dailyWeather(lat: Double, lon: Double) = apiHelper?.dailyWeather(lat, lon)


    suspend fun getAllCities() = dbHelper?.getAllCities()

    suspend fun insertCity(weather: WeatherCity) = dbHelper?.insertCity(weather)

    suspend fun cityAdded(city: String) = dbHelper?.cityAdded(city)

    suspend fun currentUserWeather(lat: Double, lng: Double) = dbHelper?.currentUserWeather(lat, lng)

    suspend fun insertUserWeather(weather: WeatherUser) = dbHelper?.insertUserWeather(weather)


}