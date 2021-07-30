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


}