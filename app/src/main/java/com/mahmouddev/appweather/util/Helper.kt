package com.mahmouddev.appweather.util

import android.app.Activity
import android.content.Context
import android.text.format.DateFormat
import android.view.inputmethod.InputMethodManager
import com.mahmouddev.appweather.R
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
     fun handleTemp(activity: Context, temp: Double): String {
         if (temp==0.0) return "Unknown"

        return if (MyPreferences.getBool(Constants.TEMP_FAHRENHEIT))
            "${activity.getString(R.string.temp)} ${temp.kelvinToFahrenheit()} F"
        else "${activity.getString(R.string.temp)} ${temp.kelvinToCelsius()} C"
    }

    fun Double.trimDouble():Double{
        return String.format("%.4f", this).toDouble()

    }


}