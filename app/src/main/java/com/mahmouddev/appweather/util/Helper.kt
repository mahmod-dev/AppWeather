package com.mahmouddev.appweather.util

import android.app.Activity
import android.os.Build
import android.text.format.DateFormat
import androidx.annotation.RequiresApi
import com.mahmouddev.appweather.R
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.*


object Helper {

    fun getFormatDate(date: Long) =
        DateFormat.format("yyyy-MMM-dd", date * 1000).toString()


    fun Double.kelvinToFahrenheit(): Double {
        val value = ((this - 273.15) * 9 / 5) + 32
        return String.format("%.2f", value).toDouble()

    }

    fun Double.kelvinToCelsius(): Double {
        val value = (this - 273.15)
        return String.format("%.2f", value).toDouble()

    }
     fun handleTemp(activity: Activity, temp: Double): String {
        return if (MyPreferences.getBool(Constants.TEMP_FAHRENHEIT))
            "${activity.getString(R.string.temp)} ${temp.kelvinToFahrenheit()} F"
        else "${activity.getString(R.string.temp)} ${temp.kelvinToCelsius()} C"
    }

}