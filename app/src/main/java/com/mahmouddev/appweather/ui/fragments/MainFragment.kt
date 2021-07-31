package com.mahmouddev.appweather.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmouddev.appweather.R
import com.mahmouddev.appweather.adapter.DailyWeatherAdapter
import com.mahmouddev.appweather.databinding.FragmentMainBinding
import com.mahmouddev.appweather.retrofit.WeatherDaysResponse
import com.mahmouddev.appweather.ui.MainActivity
import com.mahmouddev.appweather.util.Constants.LATITUDE
import com.mahmouddev.appweather.util.Constants.LONGITUDE
import com.mahmouddev.appweather.util.MyPreferences
import com.mahmouddev.appweather.util.ViewHelper.gone
import com.mahmouddev.appweather.util.ViewHelper.visible
import com.mahmouddev.appweather.util.dbUtil.Status
import com.mahmouddev.appweather.viewModel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private val TAG = "MainFragment"
    private lateinit var binding: FragmentMainBinding
    private val viewModel : WeatherViewModel by viewModels()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        MyPreferences.context = requireContext()
        val lat = MyPreferences.getFloat(LATITUDE).toDouble()
        val lng = MyPreferences.getFloat(LONGITUDE).toDouble()
        viewModel.fetchDailyWeather(lat, lng)
        setupObserverGetDailyWeather()
        searchByCity()

        (activity as MainActivity?)?.onPermissionGrant={ lat, lng ->
            viewModel.fetchDailyWeather(lat, lng)
        }

    }


    private fun setupObserverGetDailyWeather() {

        viewModel.getDailyWeather().observe(viewLifecycleOwner,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.gone()
                        it.data?.let { data ->
                            initRecycleView(data)
                            return@Observer
                        }
                        if (it.data==null)
                            Toast.makeText(requireContext(), R.string.not_found,Toast.LENGTH_SHORT).show()

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
                if (textView.length()>0)
                viewModel.fetchDailyWeather(textView.text.toString())
                else binding.etSearch.error = getString(R.string.empty)
                true
            } else false
        }
    }


}