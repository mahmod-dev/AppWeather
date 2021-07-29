package com.mahmouddev.appweather.retrofit


class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {

    override suspend fun searchByCity(cityName: String): SearchByCityResponse {
        return apiService.searchByCity(cityName)
    }

    override suspend fun dailyWeather(cityName: String): WeatherDaysResponse {
        return apiService.dailyWeather(cityName)
    }

}