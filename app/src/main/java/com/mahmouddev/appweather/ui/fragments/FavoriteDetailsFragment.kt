package com.mahmouddev.appweather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.navArgs
import com.mahmouddev.appweather.databinding.FragmentFavoriteDetailsBinding
import com.mahmouddev.appweather.util.Helper
import com.mahmouddev.appweather.util.MyPreferences


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