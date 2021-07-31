package com.mahmouddev.appweather.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.mahmouddev.appweather.retrofit.ApiHelperImpl
import com.mahmouddev.appweather.dependency.AppModule
import com.mahmouddev.appweather.dependency.AppModule.provideRetrofit
import com.mahmouddev.appweather.repository.MainRepository
import com.mahmouddev.appweather.room.AppDatabase
import com.mahmouddev.appweather.room.DatabaseHelperImpl
import com.mahmouddev.appweather.util.Constants
import com.mahmouddev.appweather.util.Helper.handleTemp
import com.mahmouddev.appweather.util.MyPreferences
import com.mahmouddev.appweather.util.NetworkHelper
import com.mahmouddev.appweather.util.NotificationHelper
import com.mahmouddev.appweather.viewModel.WeatherViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TempReceiver : BroadcastReceiver() {
    private val TAG = "TempReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        MyPreferences.context = context
        getCurrentTemp(context)

    }

    private fun getCurrentTemp(context: Context) {

        val viewModel = WeatherViewModel(
            MainRepository(
                ApiHelperImpl(AppModule.provideApiService(provideRetrofit())), DatabaseHelperImpl(
                    AppModule.buildRoomDB(context)
                )
            ), NetworkHelper(context)
        )
        val lat = MyPreferences.getFloat(Constants.LATITUDE).toDouble()
        val lng = MyPreferences.getFloat(Constants.LONGITUDE).toDouble()

        try {
            GlobalScope.launch {
                var temp = async {
                    viewModel.getTemp(lat, lng)
                }.await()

                if (temp == null) temp = 0.0

                sendNotification(context, handleTemp(context, temp))
            }
        }catch (ex:Exception){
            Log.e(TAG, "getCurrentTemp: ${ex.message}")
        }

    }

    private fun sendNotification(context: Context, temp: String) {

        NotificationHelper(context).createNotification("Temperature now is:", temp)

    }

}