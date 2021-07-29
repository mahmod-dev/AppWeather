package com.mahmouddev.appweather.util

import android.text.format.DateFormat


object Helper {

    fun getFormatDate(date: Long) =
        DateFormat.format("EEE, d MMM yyyy", date).toString()

    fun Int.kelvinToFahrenheit() = ((this - 273.15) * 9 / 5) + 32

    fun Int.kelvinToCelsius() = (this - 273.15)


}