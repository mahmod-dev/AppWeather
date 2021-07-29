package com.mahmouddev.appweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.mahmouddev.appweather.databinding.ActivityMainBinding
import com.mahmouddev.appweather.dbUtil.Status
import com.mahmouddev.appweather.dbUtil.ViewModelFactory
import com.mahmouddev.appweather.retrofit.ApiHelperImpl
import com.mahmouddev.appweather.retrofit.RetrofitBuilder
import com.mahmouddev.appweather.room.AppDatabase
import com.mahmouddev.appweather.room.DatabaseHelperImpl
import com.mahmouddev.appweather.viewModel.WeatherViewModel

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var navHostFragment: NavHostFragment
    lateinit var viewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        setupBottomBar()
        initViewModel()

        viewModel.fetchSearchByCity("gaza")
        setupObserverSearchByCity()
    }


    private fun setupBottomBar() {
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitBuilder.apiService), DatabaseHelperImpl(
                    AppDatabase.getInstance(this)
                )
            )
        ).get(WeatherViewModel::class.java)
    }


    private fun setupObserverSearchByCity() {

        viewModel.getSearchByCity().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { cities ->
                            Log.e(TAG, "setupObserverSearchByCity: ${cities}", )
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
}