package com.mahmouddev.appweather.room.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "weather_tb")
data class Weather(
    var title: String? = null,
    var details: String? = null,
    var parent: String? =null,
    var image: String? = null,
    var app: String? = null
) {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    var weatherId: Int = 0


}