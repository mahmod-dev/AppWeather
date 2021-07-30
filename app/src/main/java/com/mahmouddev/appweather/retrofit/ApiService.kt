package com.mahmouddev.appweather.retrofit


import retrofit2.http.*

interface ApiService {

    @GET("find")
    suspend fun searchByCity(@Query("q") cityName: String): SearchByCityResponse

    @GET("find")
    suspend fun currentUserLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): SearchByCityResponse

    @GET("forecast/daily")
    suspend fun dailyWeather(@Query("q") cityName: String): WeatherDaysResponse

    @GET("forecast/daily")
    suspend fun dailyWeather( @Query("lat") lat: Double,
                              @Query("lon") lon: Double): WeatherDaysResponse

}