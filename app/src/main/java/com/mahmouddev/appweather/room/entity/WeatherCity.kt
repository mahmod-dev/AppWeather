package com.mahmouddev.appweather.room.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "weather_tb")
data class WeatherCity(
    @PrimaryKey
    @NonNull
    var city: String = "",
    var temp: Double = 0.0,
    var windSpeed: Double = 0.0,
    var humidity: Int = 0,
    var pressure: Int = 0,
    var lat: Double = 0.0,
    var lng: Double = 0.0,
) : Parcelable {

    companion object {
        const val CITY = "city"
        const val TEMP = "temp"
        const val WIND_SPEED = "windSpeed"
        const val HUMIDITY = "humidity"
        const val PRESSURE = "pressure"
        const val LAT = "lat"
        const val LNG = "lng"

         val COLUMNS =listOf("[$CITY]","[$TEMP]","[$WIND_SPEED]","[$HUMIDITY]","[$PRESSURE]","[$LAT]","[$LNG]",)

    }
}