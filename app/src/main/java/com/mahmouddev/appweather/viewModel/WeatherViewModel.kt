package com.mahmouddev.appweather.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.mahmouddev.appweather.dbUtil.Resource
import com.mahmouddev.appweather.retrofit.ApiHelper
import com.mahmouddev.appweather.retrofit.SearchByCityResponse
import com.mahmouddev.appweather.retrofit.WeatherDaysResponse
import com.mahmouddev.appweather.room.DatabaseHelper
import kotlinx.coroutines.launch

class WeatherViewModel(private val apiHelper: ApiHelper?, private val dbHelper: DatabaseHelper) :
    ViewModel() {
    private val TAG = "WeatherViewModel"
    private val searchByCity = MutableLiveData<Resource<SearchByCityResponse>>()
    private val dailyWeather = MutableLiveData<Resource<WeatherDaysResponse>>()


    fun fetchSearchByCity(cityName: String) {
        viewModelScope.launch {
            searchByCity.postValue(Resource.loading(null))
            try {

                val citiesFromApi = apiHelper?.searchByCity(cityName)

                if (citiesFromApi?.cod == "200" && !citiesFromApi.list.isNullOrEmpty()) {
                    searchByCity.postValue(Resource.success(citiesFromApi))
                } else {
                    searchByCity.postValue(Resource.success(null))
                }
            } catch (e: Exception) {
                Log.e(TAG, "fetchSearchByCity: ${e.message}")

                searchByCity.postValue(Resource.error("Something error!", null))
            }
        }
    }

    fun fetchDailyWeather(cityName: String) {
        viewModelScope.launch {
            dailyWeather.postValue(Resource.loading(null))
            try {

                val citiesFromApi = apiHelper?.dailyWeather(cityName)

                if (citiesFromApi?.cod == "200" && !citiesFromApi.list.isNullOrEmpty()) {
                    dailyWeather.postValue(Resource.success(citiesFromApi))
                } else {
                    dailyWeather.postValue(Resource.success(null))
                }
            } catch (e: Exception) {
                Log.e(TAG, "fetchDailyWeather: ${e.message}")

                dailyWeather.postValue(Resource.error("Something error!", null))
            }
        }
    }


    fun getSearchByCity(): LiveData<Resource<SearchByCityResponse>> {
        return searchByCity
    }

    fun getDailyWeather(): LiveData<Resource<WeatherDaysResponse>> {
        return dailyWeather
    }

}