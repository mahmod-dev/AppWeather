package com.mahmouddev.appweather.room.entity


import androidx.annotation.NonNull
import androidx.room.Entity

@Entity(tableName = "current_user_tb",primaryKeys = ["lat", "lng"])
data class WeatherUser(
    var city: String = "",
    var temp: Double = 0.0,
    var windSpeed: Double = 0.0,    var humidity: Int =0,
    var pressure: Int =0,
    @NonNull
    var lat: Double = 0.0,
    @NonNull
    var lng: Double = 0.0,
) {


}