package com.mahmouddev.appweather.retrofit


class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {

    override suspend fun searchByCity(cityName: String): SearchByCityResponse {
        return apiService.searchByCity(cityName)
    }

    override suspend fun dailyWeather(cityName: String): WeatherDaysResponse {
        return apiService.dailyWeather(cityName)
    }

    override suspend fun dailyWeather(lat: Double, lon: Double): WeatherDaysResponse {
        return apiService.dailyWeather(lat, lon)
    }

    override suspend fun currentUserLocation(lat: Double, lon: Double): SearchByCityResponse {
        return apiService.currentUserLocation(lat, lon)
    }

}