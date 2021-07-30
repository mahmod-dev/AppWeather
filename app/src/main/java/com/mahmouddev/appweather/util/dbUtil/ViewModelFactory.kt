package com.mahmouddev.appweather.util.dbUtil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mahmouddev.appweather.retrofit.ApiHelper
import com.mahmouddev.appweather.room.DatabaseHelper
import com.mahmouddev.appweather.viewModel.WeatherViewModel

class ViewModelFactory(private val apiHelper: ApiHelper, val dbHelper: DatabaseHelper) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(apiHelper,dbHelper) as T
        }

        throw IllegalArgumentException("Unknown class name")
    }


}