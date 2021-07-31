package com.mahmouddev.appweather.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.mahmouddev.appweather.R
import com.mahmouddev.appweather.databinding.FragmentSettingsBinding
import com.mahmouddev.appweather.util.Constants.TEMP_FAHRENHEIT
import com.mahmouddev.appweather.util.MyPreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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