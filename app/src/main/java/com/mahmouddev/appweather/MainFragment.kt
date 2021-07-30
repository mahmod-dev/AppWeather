package com.mahmouddev.appweather

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmouddev.appweather.adapter.DailyWeatherAdapter
import com.mahmouddev.appweather.databinding.FragmentMainBinding
import com.mahmouddev.appweather.dbUtil.Status
import com.mahmouddev.appweather.dbUtil.ViewModelFactory
import com.mahmouddev.appweather.retrofit.ApiHelperImpl
import com.mahmouddev.appweather.retrofit.RetrofitBuilder
import com.mahmouddev.appweather.retrofit.WeatherDaysResponse
import com.mahmouddev.appweather.room.AppDatabase
import com.mahmouddev.appweather.room.DatabaseHelperImpl
import com.mahmouddev.appweather.util.Constants.LATITUDE
import com.mahmouddev.appweather.util.Constants.LONGITUDE
import com.mahmouddev.appweather.util.MyPreferences
import com.mahmouddev.appweather.util.ViewHelper.gone
import com.mahmouddev.appweather.util.ViewHelper.visible
import com.mahmouddev.appweather.viewModel.WeatherViewModel


class MainFragment : Fragment(R.layout.fragment_main) {
    private val TAG = "MainFragment"
    private lateinit var binding: FragmentMainBinding

    lateinit var viewModel: WeatherViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        initViewModel()
        MyPreferences.context = requireContext()
        val lat = MyPreferences.getFloat(LATITUDE).toDouble()
        val lng = MyPreferences.getFloat(LONGITUDE).toDouble()
        viewModel.fetchDailyWeather(lat, lng)
        setupObserverGetDailyWeather()
        searchByCity()
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
                        binding.progressBar.gone()
                        it.data?.let { data ->
                            Log.e(TAG, "setupObserverGetDailyWeather: ${data}")
                            initRecycleView(data)
                        }
                    }
                    Status.LOADING -> {
                        binding.progressBar.visible()

                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                        binding.progressBar.gone()
                    }
                }

            }
        )
    }


    private fun initRecycleView(data: WeatherDaysResponse) {

        val weatherAdapter = DailyWeatherAdapter(requireActivity(), data)
        binding.rvWeather.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = weatherAdapter
            setHasFixedSize(true)
        }

    }

    private fun searchByCity() {
        binding.etSearch.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                Log.e(TAG, "searchByCity: ${textView.text}",)
                viewModel.fetchDailyWeather(textView.text.toString())
                true
            } else false
        }
    }



}