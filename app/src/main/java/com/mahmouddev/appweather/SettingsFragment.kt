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
import com.mahmouddev.appweather.databinding.FragmentSettingsBinding
import com.mahmouddev.appweather.dbUtil.Status
import com.mahmouddev.appweather.dbUtil.ViewModelFactory
import com.mahmouddev.appweather.retrofit.ApiHelperImpl
import com.mahmouddev.appweather.retrofit.RetrofitBuilder
import com.mahmouddev.appweather.retrofit.WeatherDaysResponse
import com.mahmouddev.appweather.room.AppDatabase
import com.mahmouddev.appweather.room.DatabaseHelperImpl
import com.mahmouddev.appweather.room.entity.WeatherCity
import com.mahmouddev.appweather.util.Constants
import com.mahmouddev.appweather.util.Constants.TEMP_FAHRENHEIT
import com.mahmouddev.appweather.util.MyPreferences
import com.mahmouddev.appweather.util.ViewHelper.gone
import com.mahmouddev.appweather.util.ViewHelper.visible
import com.mahmouddev.appweather.viewModel.WeatherViewModel


class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private lateinit var binding: FragmentSettingsBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)
        MyPreferences.context = requireContext()

        binding.swTemp.isChecked = MyPreferences.getBool(TEMP_FAHRENHEIT)


        binding.swTemp.setOnCheckedChangeListener { buttonView, isChecked ->
            MyPreferences.setBool(TEMP_FAHRENHEIT, isChecked)
        }

    }


}