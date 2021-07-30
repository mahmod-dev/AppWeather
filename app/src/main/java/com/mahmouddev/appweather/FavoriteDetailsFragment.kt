package com.mahmouddev.appweather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmouddev.appweather.adapter.DailyWeatherAdapter
import com.mahmouddev.appweather.adapter.FavoriteAdapter
import com.mahmouddev.appweather.databinding.FragmentFavoriteBinding
import com.mahmouddev.appweather.databinding.FragmentFavoriteDetailsBinding
import com.mahmouddev.appweather.dbUtil.Status
import com.mahmouddev.appweather.dbUtil.ViewModelFactory
import com.mahmouddev.appweather.retrofit.ApiHelperImpl
import com.mahmouddev.appweather.retrofit.RetrofitBuilder
import com.mahmouddev.appweather.retrofit.WeatherDaysResponse
import com.mahmouddev.appweather.room.AppDatabase
import com.mahmouddev.appweather.room.DatabaseHelperImpl
import com.mahmouddev.appweather.room.entity.WeatherCity
import com.mahmouddev.appweather.util.Constants
import com.mahmouddev.appweather.util.Helper
import com.mahmouddev.appweather.util.Helper.kelvinToCelsius
import com.mahmouddev.appweather.util.Helper.kelvinToFahrenheit
import com.mahmouddev.appweather.util.MyPreferences
import com.mahmouddev.appweather.util.ViewHelper.gone
import com.mahmouddev.appweather.util.ViewHelper.visible
import com.mahmouddev.appweather.viewModel.WeatherViewModel


class FavoriteDetailsFragment : Fragment(R.layout.fragment_favorite_details) {
    private lateinit var binding: FragmentFavoriteDetailsBinding
    private val args: FavoriteDetailsFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteDetailsBinding.bind(view)
        val weather = args.weatherCity
        MyPreferences.context = requireContext()

        binding.apply {
            tvHumidity.text =
                "${requireActivity().getString(R.string.humidity)} ${weather.humidity}"
            tvPressure.text =
                "${requireActivity().getString(R.string.pressure)} ${weather.pressure}"
            tvCity.text = "${requireActivity().getString(R.string.city)} ${weather.city}"
            tvWind.text = "${requireActivity().getString(R.string.wind_speed)} ${weather.windSpeed}"
            tvLatLng.text =
                "${requireActivity().getString(R.string.lat_lng)} ${weather.lat}, ${weather.lng}"

            tvTemp.text = Helper.handleTemp(requireActivity(),weather.temp)


        }

    }


}