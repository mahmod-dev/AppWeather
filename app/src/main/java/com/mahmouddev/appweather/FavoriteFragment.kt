package com.mahmouddev.appweather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmouddev.appweather.adapter.FavoriteAdapter
import com.mahmouddev.appweather.databinding.FragmentFavoriteBinding
import com.mahmouddev.appweather.dbUtil.Status
import com.mahmouddev.appweather.dbUtil.ViewModelFactory
import com.mahmouddev.appweather.retrofit.ApiHelperImpl
import com.mahmouddev.appweather.retrofit.RetrofitBuilder
import com.mahmouddev.appweather.room.AppDatabase
import com.mahmouddev.appweather.room.DatabaseHelperImpl
import com.mahmouddev.appweather.room.entity.WeatherCity
import com.mahmouddev.appweather.util.ViewHelper.gone
import com.mahmouddev.appweather.util.ViewHelper.visible
import com.mahmouddev.appweather.viewModel.WeatherViewModel


class FavoriteFragment : Fragment(R.layout.fragment_favorite) {
    private lateinit var binding: FragmentFavoriteBinding
    lateinit var viewModel: WeatherViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteBinding.bind(view)
        initViewModel()
        viewModel.fetchFavWeather()
        setupObserverSearchByCity()

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

    private fun initRecycleView(data:  List<WeatherCity>) {

        val favoriteAdapter = FavoriteAdapter(requireActivity(), data)
        binding.rvWeather.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteAdapter
            setHasFixedSize(true)
        }

        favoriteAdapter.onItemClick= {
            navToFragmentDetails(it)
        }

    }

    private fun setupObserverSearchByCity() {

        viewModel.getFavoriteWeather().observe(viewLifecycleOwner,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.gone()

                        if (!it.data.isNullOrEmpty()){
                            initRecycleView(it.data)
                        }else
                            binding.tvNotFound.visible()

                    }
                    Status.LOADING -> {
                        binding.progressBar.visible()

                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.progressBar.gone()
                    }
                }

            }
        )
    }

    private fun navToFragmentDetails(weatherCity: WeatherCity){
        val action = FavoriteFragmentDirections.actionNavFavoriteFragmentToFavoriteDetailsFragment(weatherCity)
        findNavController().navigate(action)
    }

}