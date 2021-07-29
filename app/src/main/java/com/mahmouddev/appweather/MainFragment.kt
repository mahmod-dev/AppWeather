package com.mahmouddev.appweather

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmouddev.appweather.adapter.DailyWeatherAdapter
import com.mahmouddev.appweather.databinding.FragmentFavoriteBinding
import com.mahmouddev.appweather.databinding.FragmentMainBinding
import com.mahmouddev.appweather.dbUtil.Status
import com.mahmouddev.appweather.dbUtil.ViewModelFactory
import com.mahmouddev.appweather.retrofit.ApiHelperImpl
import com.mahmouddev.appweather.retrofit.DataDay
import com.mahmouddev.appweather.retrofit.RetrofitBuilder
import com.mahmouddev.appweather.retrofit.WeatherDaysResponse
import com.mahmouddev.appweather.room.AppDatabase
import com.mahmouddev.appweather.room.DatabaseHelperImpl
import com.mahmouddev.appweather.viewModel.WeatherViewModel


class MainFragment : Fragment(R.layout.fragment_main) {
    private val TAG = "MainFragment"
    private lateinit var binding: FragmentMainBinding

    lateinit var viewModel: WeatherViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        initViewModel()
        viewModel.fetchDailyWeather("gaza")
        setupObserverGetDailyWeather()
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService), DatabaseHelperImpl(
                    AppDatabase.getInstance(requireContext())
                )
            )
        ).get(WeatherViewModel::class.java)
    }


    private fun setupObserverGetDailyWeather() {

        viewModel.getDailyWeather().observe(viewLifecycleOwner,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { data ->
                            Log.e(TAG, "setupObserverGetDailyWeather: ${data}")
                            initRecycleView(data.list)
                        }
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {

                    }
                }

            }
        )
    }


    private fun initRecycleView(data: ArrayList<DataDay>) {

        val callLogAdapter = DailyWeatherAdapter(requireActivity(), data)
        binding.rvWeather.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = callLogAdapter
            setHasFixedSize(true)
        }


    }
}