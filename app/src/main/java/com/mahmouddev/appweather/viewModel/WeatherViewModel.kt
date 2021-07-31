package com.mahmouddev.appweather.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.mahmouddev.appweather.repository.MainRepository
import com.mahmouddev.appweather.util.dbUtil.Resource
import com.mahmouddev.appweather.retrofit.WeatherDaysResponse
import com.mahmouddev.appweather.room.entity.WeatherCity
import com.mahmouddev.appweather.room.entity.WeatherUser
import com.mahmouddev.appweather.util.Constants.CODE_STATUS
import com.mahmouddev.appweather.util.Helper.trimDouble
import com.mahmouddev.appweather.util.NetworkHelper
import kotlinx.coroutines.launch
import retrofit2.HttpException

class WeatherViewModel @ViewModelInject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) :
    ViewModel() {
    private val TAG = "WeatherViewModel"
    private val searchByCity = MutableLiveData<Resource<WeatherCity>>()
    private val dailyWeather = MutableLiveData<Resource<WeatherDaysResponse>>()
    private val currentUserWeather = MutableLiveData<Resource<WeatherUser>>()
    private val favoriteWeather = MutableLiveData<Resource<List<WeatherCity>>>()
    private val insertFavoriteWeather = MutableLiveData<Resource<List<WeatherCity>>>()


    fun fetchSearchByCity(cityName: String) {
        viewModelScope.launch {
            searchByCity.postValue(Resource.loading(null))
            try {
                if (networkHelper.isNetworkConnected()) {
                    val citiesFromApi = mainRepository.searchByCity(cityName)

                    if (citiesFromApi?.cod == CODE_STATUS && !citiesFromApi.list.isNullOrEmpty()) {
                        val weatherCity = WeatherCity(
                            citiesFromApi.list[0].name,
                            citiesFromApi.list[0].main.temp,
                            citiesFromApi.list[0].wind.speed,
                            citiesFromApi.list[0].main.humidity,
                            citiesFromApi.list[0].main.pressure,
                            citiesFromApi.list[0].coord.lat,
                            citiesFromApi.list[0].coord.lon,
                            citiesFromApi.list[0].weather[0].description,

                            )
                        searchByCity.postValue(Resource.success(weatherCity))
                    } else {
                        searchByCity.postValue(Resource.success(null))
                    }
                } else
                    searchByCity.postValue(Resource.error("No internet connection", null))

            } catch (e: Exception) {
                if (e is HttpException) {
                    searchByCity.postValue(Resource.error("Not found city!", null))
                    return@launch
                }
                searchByCity.postValue(Resource.error("Something error!", null))
            }
        }
    }

    fun fetchDailyWeather(cityName: String) {
        viewModelScope.launch {

            dailyWeather.postValue(Resource.loading(null))
            try {

                if (networkHelper.isNetworkConnected()) {
                    val citiesFromApi = mainRepository.dailyWeather(cityName)

                    if (citiesFromApi?.cod == CODE_STATUS && !citiesFromApi.list.isNullOrEmpty()) {
                        dailyWeather.postValue(Resource.success(citiesFromApi))
                    } else {
                        dailyWeather.postValue(Resource.success(null))
                    }
                } else
                    dailyWeather.postValue(Resource.error("No internet connection", null))

            } catch (e: Exception) {
                if (e is HttpException) {
                    dailyWeather.postValue(Resource.error("Not found city!", null))
                    return@launch
                }
                dailyWeather.postValue(Resource.error("Something error!", null))
            }

        }
    }

    fun fetchDailyWeather(lat: Double, lng: Double) {
        if (lat == 0.0 && lng == 0.0) return

        viewModelScope.launch {

            dailyWeather.postValue(Resource.loading(null))
            try {
                if (networkHelper.isNetworkConnected()) {
                    val citiesFromApi = mainRepository.dailyWeather(lat, lng)

                    if (citiesFromApi?.cod == CODE_STATUS && !citiesFromApi.list.isNullOrEmpty()) {
                        dailyWeather.postValue(Resource.success(citiesFromApi))
                    } else {
                        dailyWeather.postValue(Resource.success(null))
                    }
                } else
                    dailyWeather.postValue(Resource.error("No internet connection", null))
            } catch (e: Exception) {
                if (e is HttpException) {
                    dailyWeather.postValue(Resource.error("Not found city!", null))
                    return@launch
                }
                dailyWeather.postValue(Resource.error("Something error!", null))
            }

        }
    }


    fun insertFavCity(weather: WeatherCity) {
        viewModelScope.launch {
            insertFavoriteWeather.postValue(Resource.loading(null))
            try {
                if (networkHelper.isNetworkConnected()) {

                    val citiesFromApi = mainRepository.insertCity(weather)
                    if (citiesFromApi != null) {
                        if (citiesFromApi > 0)
                            insertFavoriteWeather.postValue(Resource.success(null))
                        else
                            insertFavoriteWeather.postValue(
                                Resource.error(
                                    "Something error!",
                                    null
                                )
                            )
                    }

                } else
                    insertFavoriteWeather.postValue(Resource.error("No internet connection", null))
            } catch (e: Exception) {
                if (e is HttpException) {
                    insertFavoriteWeather.postValue(Resource.error("Not found city!", null))
                    return@launch
                }
                insertFavoriteWeather.postValue(Resource.error("Something error!", null))
            }
        }
    }

    fun fetchCurrentUserWeather(lat: Double, lng: Double) {
        if (lat == 0.0 && lng == 0.0) return
        val latitude = lat.trimDouble()
        val longitude = lng.trimDouble()
        viewModelScope.launch {
            currentUserWeather.postValue(Resource.loading(null))
            try {
                if (networkHelper.isNetworkConnected()) {
                    var userWeatherDB = mainRepository.currentUserWeather(latitude, longitude)
                    if (userWeatherDB != null) {
                        currentUserWeather.postValue(Resource.success(userWeatherDB))
                    }
                    val currentUserAPi = mainRepository.currentUserLocation(latitude, longitude)
                    if (currentUserAPi != null) {
                        val weather = WeatherUser(
                            currentUserAPi.list[0].name,
                            currentUserAPi.list[0].main.temp,
                            currentUserAPi.list[0].wind.speed,
                            currentUserAPi.list[0].main.humidity,
                            currentUserAPi.list[0].main.pressure,
                            latitude, longitude,
                        )
                        mainRepository.insertUserWeather(weather)
                    }
                    userWeatherDB = mainRepository.currentUserWeather(latitude, longitude)
                    currentUserWeather.postValue(Resource.success(userWeatherDB))
                } else
                    currentUserWeather.postValue(Resource.error("No internet connection", null))

            } catch (e: Exception) {
                if (e is HttpException) {
                    currentUserWeather.postValue(Resource.error("Not found city!", null))
                    return@launch
                }
                currentUserWeather.postValue(Resource.error("Something error!", null))
            }
        }
    }

    fun fetchFavWeather() {
        viewModelScope.launch {
            favoriteWeather.postValue(Resource.loading(null))
            try {
                if (networkHelper.isNetworkConnected()) {

                    val userWeatherDB = mainRepository.getAllCities()
                    favoriteWeather.postValue(Resource.success(userWeatherDB))
                } else
                    favoriteWeather.postValue(Resource.error("No internet connection", null))

            } catch (e: Exception) {
                if (e is HttpException) {
                    favoriteWeather.postValue(Resource.error("Not found city!", null))
                    return@launch
                }
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

    suspend fun getTemp(lat: Double, lng: Double): Double? {
        if (lat == 0.0 && lng == 0.0) return null

        val currentUserAPi = mainRepository.currentUserLocation(lat, lng)

        return if (currentUserAPi != null)
            currentUserAPi.list[0].main.temp
        else null

    }


}