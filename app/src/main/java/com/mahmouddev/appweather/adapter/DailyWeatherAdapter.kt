package com.mahmouddev.appweather.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mahmouddev.appweather.R
import com.mahmouddev.appweather.databinding.ItemWeatherBinding
import com.mahmouddev.appweather.retrofit.DataDay
import com.mahmouddev.appweather.retrofit.WeatherDaysResponse
import com.mahmouddev.appweather.util.Constants
import com.mahmouddev.appweather.util.Helper
import com.mahmouddev.appweather.util.Helper.kelvinToCelsius
import com.mahmouddev.appweather.util.Helper.kelvinToFahrenheit
import com.mahmouddev.appweather.util.MyPreferences

class DailyWeatherAdapter(var activity: Activity, private var data: WeatherDaysResponse) :

    RecyclerView.Adapter<DailyWeatherAdapter.MyViewHolder>() {
    val TAG = "DailyWeatherAdapter"


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
        val viewBinding = ItemWeatherBinding.inflate(view, viewGroup, false)

        return MyViewHolder(viewBinding)

    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {

        myViewHolder.bind(data)
    }

    override fun getItemCount(): Int {
        return data.list.size
    }

    inner class MyViewHolder(private var binding: ItemWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(weather: WeatherDaysResponse) {
            val data = data.list[adapterPosition]
            binding.apply {
                Log.e(TAG, "bind: ${data.dt}", )
                tvDay.text = Helper.getFormatDate(data.dt)
                tvHumidity.text = "${activity.getString(R.string.humidity)} ${data.humidity}"
                tvPressure.text = "${activity.getString(R.string.pressure)} ${data.pressure}"
                tvCity.text = "${activity.getString(R.string.city)} ${weather.city.name}"

                    tvTemp.text = Helper.handleTemp(activity,data.temp.day)

            }

        }

    }


}