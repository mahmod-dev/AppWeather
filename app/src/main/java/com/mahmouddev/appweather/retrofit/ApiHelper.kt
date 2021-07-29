package com.mahmouddev.appweather.retrofit



interface ApiHelper {

    suspend fun searchByCity(cityName: String): SearchByCityResponse


    suspend fun dailyWeather( cityName: String): WeatherDaysResponse


}