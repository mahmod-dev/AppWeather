package com.mahmouddev.appweather

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmouddev.appweather.adapter.DailyWeatherAdapter
import com.mahmouddev.appweather.adapter.FavoriteAdapter
import com.mahmouddev.appweather.databinding.FragmentFavoriteBinding
import com.mahmouddev.appweather.databinding.FragmentSearchBinding
import com.mahmouddev.appweather.dbUtil.Status
import com.mahmouddev.appweather.dbUtil.ViewModelFactory
import com.mahmouddev.appweather.retrofit.ApiHelperImpl
import com.mahmouddev.appweather.retrofit.RetrofitBuilder
import com.mahmouddev.appweather.retrofit.WeatherDaysResponse
import com.mahmouddev.appweather.room.AppDatabase
import com.mahmouddev.appweather.room.DatabaseHelperImpl
import com.mahmouddev.appweather.room.entity.WeatherCity
import com.mahmouddev.appweather.util.ViewHelper.gone
import com.mahmouddev.appweather.util.ViewHelper.visible
import com.mahmouddev.appweather.viewModel.WeatherViewModel


class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding
    lateinit var viewModel: WeatherViewModel
    lateinit var witherCity :WeatherCity


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        initViewModel()
        searchByCity()
        setupObserverSearchByCity()
        registerForContextMenu(binding.container)

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


    private fun searchByCity() {
        binding.etSearch.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.fetchSearchByCity(textView.text.toString())
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
                        it.data?.let { data ->
                            witherCity = data
                            binding.tvHumidity.text =
                                "${requireActivity().getString(R.string.humidity)} ${data.humidity}"
                            binding.tvPressure.text =
                                "${requireActivity().getString(R.string.pressure)} ${data.pressure}"
                            binding.tvTemp.text =
                                "${requireActivity().getString(R.string.temp)} ${data.temp}"
                            binding.tvCity.text =
                                "${requireActivity().getString(R.string.city)} ${data.city}"
                        }
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

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menu.add(0,1,0,requireActivity().getString(R.string.add_to_fav))

       // requireActivity().menuInflater.inflate(R.menu.context_menu,menu)

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            1->{
                viewModel.insertFavCity(witherCity)
                Toast.makeText(requireContext(),"${item.title}", Toast.LENGTH_SHORT).show()

            }

        }
        return super.onContextItemSelected(item)
    }
}