package com.mahmouddev.appweather.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmouddev.appweather.R
import com.mahmouddev.appweather.adapter.FavoriteAdapter
import com.mahmouddev.appweather.databinding.FragmentFavoriteBinding
import com.mahmouddev.appweather.util.dbUtil.Status
import com.mahmouddev.appweather.room.entity.WeatherCity
import com.mahmouddev.appweather.util.ViewHelper.gone
import com.mahmouddev.appweather.util.ViewHelper.visible
import com.mahmouddev.appweather.viewModel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {
    private lateinit var binding: FragmentFavoriteBinding
    private val viewModel : WeatherViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteBinding.bind(view)
        viewModel.fetchFavWeather()
        setupObserverSearchByCity()

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