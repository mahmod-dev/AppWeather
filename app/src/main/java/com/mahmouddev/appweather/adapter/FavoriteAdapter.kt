package com.mahmouddev.appweather.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mahmouddev.appweather.R
import com.mahmouddev.appweather.databinding.ItemFavoriteBinding
import com.mahmouddev.appweather.room.entity.WeatherCity
import com.mahmouddev.appweather.util.Helper
import com.mahmouddev.appweather.util.MyPreferences

class FavoriteAdapter(var activity: Activity, private var data: List<WeatherCity>) :
    RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {
    val TAG = "FavoriteAdapter"
    var onItemClick: ((WeatherCity) -> Unit)? = null

    init {
        MyPreferences.context = activity
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
        val viewBinding = ItemFavoriteBinding.inflate(view, viewGroup, false)

        return MyViewHolder(viewBinding)

    }

    override fun onBindViewHolder(myViewHolder: MyViewHolder, i: Int) {

        myViewHolder.bind(data[i])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class MyViewHolder(private var binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(weather: WeatherCity) {
            binding.apply {
                //  Log.e(TAG, "bind: ${weather.d}", )
                tvHumidity.text = "${activity.getString(R.string.humidity)} ${weather.humidity}"
                tvPressure.text = "${activity.getString(R.string.pressure)} ${weather.pressure}"
                tvCity.text = "${activity.getString(R.string.city)} ${weather.city}"

                tvTemp.text = Helper.handleTemp(activity, weather.temp)

                container.setOnClickListener {
                    onItemClick?.invoke(weather)
                }
            }

        }

    }


}