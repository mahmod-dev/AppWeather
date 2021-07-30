package com.mahmouddev.appweather.retrofit



interface ApiHelper {

    suspend fun searchByCity(cityName: String): SearchByCityResponse

    suspend fun dailyWeather( cityName: String): WeatherDaysResponse

    suspend fun currentUserLocation(lat: Double, lon: Double): SearchByCityResponse

    suspend fun dailyWeather( lat: Double, lon: Double): WeatherDaysResponse
}