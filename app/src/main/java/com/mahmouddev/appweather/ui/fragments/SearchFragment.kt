package com.mahmouddev.appweather.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mahmouddev.appweather.R
import com.mahmouddev.appweather.databinding.FragmentSearchBinding
import com.mahmouddev.appweather.util.dbUtil.Status
import com.mahmouddev.appweather.room.entity.WeatherCity
import com.mahmouddev.appweather.util.Helper
import com.mahmouddev.appweather.util.ViewHelper.gone
import com.mahmouddev.appweather.util.ViewHelper.visible
import com.mahmouddev.appweather.viewModel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private val viewModel: WeatherViewModel by viewModels()

    private lateinit var binding: FragmentSearchBinding

    //  lateinit var viewModel: WeatherViewModel
    lateinit var witherCity: WeatherCity


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        searchByCity()
        setupObserverSearchByCity()
        registerForContextMenu(binding.container)
    }



    private fun searchByCity() {
        binding.etSearch.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (textView.length() > 0)
                    viewModel.fetchSearchByCity(textView.text.toString())
                else binding.etSearch.error = getString(R.string.empty)
                true
            } else false
        }
    }

    private fun setupObserverSearchByCity() {

        viewModel.getSearchByCity().observe(viewLifecycleOwner,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.gone()
                        if (it.data != null) {
                            binding.container.visible()
                            val data = it.data
                            witherCity = data
                            binding.tvHumidity.text =
                                "${requireActivity().getString(R.string.humidity)} ${data.humidity}"
                            binding.tvPressure.text =
                                "${requireActivity().getString(R.string.pressure)} ${data.pressure}"
                            binding.tvTemp.text =
                                Helper.handleTemp(requireActivity(), data.temp)
                            binding.tvCity.text =
                                "${requireActivity().getString(R.string.city)} ${data.city}"
                        } else {
                            Toast.makeText(requireContext(), R.string.not_found, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    Status.LOADING -> {
                        binding.container.gone()
                        binding.progressBar.visible()

                    }
                    Status.ERROR -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.container.gone()
                        binding.progressBar.gone()
                    }
                }

            }
        )
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menu.add(0, 1, 0, requireActivity().getString(R.string.add_to_fav))

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            1 -> {
                viewModel.insertFavCity(witherCity)
                Toast.makeText(requireContext(), "${item.title}", Toast.LENGTH_SHORT).show()
            }

        }
        return super.onContextItemSelected(item)
    }


}