package com.mahmouddev.appweather.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.mahmouddev.appweather.dbUtil.Resource
import com.mahmouddev.appweather.retrofit.ApiHelper
import com.mahmouddev.appweather.retrofit.SearchByCityResponse
import com.mahmouddev.appweather.retrofit.WeatherDaysResponse
import com.mahmouddev.appweather.room.DatabaseHelper
import com.mahmouddev.appweather.room.entity.WeatherCity
import com.mahmouddev.appweather.room.entity.WeatherUser
import kotlinx.coroutines.launch

class WeatherViewModel(private val apiHelper: ApiHelper?, private val dbHelper: DatabaseHelper) :
    ViewModel() {
    private val TAG = "WeatherViewModel"
    private val searchByCity = MutableLiveData<Resource<WeatherCity>>()
    private val dailyWeather = MutableLiveData<Resource<WeatherDaysResponse>>()
    private val insertUserWeather = MutableLiveData<Resource<WeatherUser>>()
    private val currentUserWeather = MutableLiveData<Resource<WeatherUser>>()
    private val favoriteWeather = MutableLiveData<Resource<List<WeatherCity>>>()
    private val insertFavoriteWeather = MutableLiveData<Resource<List<WeatherCity>>>()


     fun fetchSearchByCity(cityName: String) {
         viewModelScope.launch {
             searchByCity.postValue(Resource.loading(null))
             try {

                 val citiesFromApi = apiHelper?.searchByCity(cityName)

                 if (citiesFromApi?.cod == "200" && !citiesFromApi.list.isNullOrEmpty()) {
                   val weatherCity = WeatherCity(
                       citiesFromApi.list[0].name,
                       citiesFromApi.list[0].main.temp,
                       citiesFromApi.list[0].wind.speed,
                       citiesFromApi.list[0].main.humidity,
                       citiesFromApi.list[0].main.pressure,
                       citiesFromApi.list[0].coord.lat,
                       citiesFromApi.list[0].coord.lon,

                       )
                     searchByCity.postValue(Resource.success(weatherCity))
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

    fun fetchDailyWeather(lat: Double, lng: Double) {
        viewModelScope.launch {
            dailyWeather.postValue(Resource.loading(null))
            try {

                val citiesFromApi = apiHelper?.dailyWeather(lat,lng)

                if (citiesFromApi?.cod == "200" && !citiesFromApi.list.isNullOrEmpty()) {
                    dailyWeather.postValue(Resource.success(citiesFromApi))
                } else {
                    dailyWeather.postValue(Resource.success(null))
                }
            } catch (e: Exception) {
                Log.e(TAG, "fetchDailyWeather: ${e.message}")

                dailyWeather.postValue(e.message?.let { Resource.error(it, null) })
            }
        }
    }


    fun insertCurrentUserWeather(weather: WeatherUser) {
        viewModelScope.launch {
            insertUserWeather.postValue(Resource.loading(null))
            try {

                val citiesFromApi = dbHelper.insertUserWeather(weather)
                if (citiesFromApi > 0)
                    insertUserWeather.postValue(Resource.success(null))

            } catch (e: Exception) {
                Log.e(TAG, "insertCurrentUserWeather: ${e.message}")

                insertUserWeather.postValue(Resource.error("Something error!", null))
            }
        }
    }

    fun insertFavCity(weather: WeatherCity) {
        viewModelScope.launch {
            insertFavoriteWeather.postValue(Resource.loading(null))
            try {

                val citiesFromApi = dbHelper.insertCity(weather)
                if (citiesFromApi > 0)
                    insertFavoriteWeather.postValue(Resource.success(null))

            } catch (e: Exception) {
                Log.e(TAG, "insertFavCity: ${e.message}")

                insertFavoriteWeather.postValue(Resource.error("Something error!", null))
            }
        }
    }

    fun fetchCurrentUserWeather(lat: Double, lng: Double) {
        viewModelScope.launch {
            currentUserWeather.postValue(Resource.loading(null))
            try {

                var userWeatherDB = dbHelper.currentUserWeather(lat, lng)
                if (userWeatherDB == null) {
                    val currentUserAPi = apiHelper?.currentUserLocation(lat, lng)
                    if (currentUserAPi != null) {
                        val weather = WeatherUser(
                            currentUserAPi.list[0].name,
                            currentUserAPi.list[0].main.temp,
                            currentUserAPi.list[0].wind.speed,
                            currentUserAPi.list[0].main.humidity,
                            currentUserAPi.list[0].main.pressure,
                            lat, lng,)
                        dbHelper.insertUserWeather(weather)
                    }

                }
                 userWeatherDB = dbHelper.currentUserWeather(lat, lng)
                currentUserWeather.postValue(Resource.success(userWeatherDB))


            } catch (e: Exception) {
                Log.e(TAG, "getCurrentUserWeather: ${e.message}")

                currentUserWeather.postValue(Resource.error("Something error!", null))
            }
        }
    }

    fun fetchFavWeather() {
        viewModelScope.launch {
            favoriteWeather.postValue(Resource.loading(null))
            try {

                val userWeatherDB = dbHelper.getAllCities()
                favoriteWeather.postValue(Resource.success(userWeatherDB))

            } catch (e: Exception) {
                Log.e(TAG, "fetchFavWeather: ${e.message}")

                favoriteWeather.postValue(Resource.error("Something error!", null))
            }
        }
    }

     fun getSearchByCity(): LiveData<Resource<WeatherCity>> {
         return searchByCity
     }

    fun getDailyWeather(): LiveData<Resource<WeatherDaysResponse>> {
        return dailyWeather
    }

    fun getFavoriteWeather(): LiveData<Resource<List<WeatherCity>>> {
        return favoriteWeather
    }

    fun getCurrentUserWeather(): LiveData<Resource<WeatherUser>> {
        return currentUserWeather
    }

}