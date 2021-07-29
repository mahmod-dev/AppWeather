package com.mahmouddev.appweather.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mahmouddev.appweather.R
import com.mahmouddev.appweather.databinding.ItemWeatherBinding
import com.mahmouddev.appweather.retrofit.DataDay
import com.mahmouddev.appweather.util.Helper

class DailyWeatherAdapter(var activity: Activity, var data: ArrayList<DataDay>) :

    RecyclerView.Adapter<DailyWeatherAdapter.MyViewHolder>() {
    val TAG = "DailyWeatherAdapter"


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
        val viewBinding = ItemWeatherBinding.inflate(view, viewGroup, false)

        return MyViewHolder(viewBinding)

    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {

        myViewHolder.bind(data[i])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(var binding: ItemWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(weather: DataDay) {

            binding.apply {
                tvDay.text = Helper.getFormatDate(weather.dt)
                tvHumidity.text = "${activity.getString(R.string.humidity)} ${weather.humidity}"
                tvPressure.text = "${activity.getString(R.string.pressure)} ${weather.pressure}"
                tvTemp.text = "${activity.getString(R.string.temp)} ${weather.temp.day}"
            }

        }

    }


}