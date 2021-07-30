package com.mahmouddev.appweather

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.android.talabaty.util.LocationManager
import com.mahmouddev.appweather.databinding.ActivityMainBinding
import com.mahmouddev.appweather.dbUtil.Status
import com.mahmouddev.appweather.dbUtil.ViewModelFactory
import com.mahmouddev.appweather.location.LocationHelper
import com.mahmouddev.appweather.retrofit.ApiHelperImpl
import com.mahmouddev.appweather.retrofit.RetrofitBuilder
import com.mahmouddev.appweather.room.AppDatabase
import com.mahmouddev.appweather.room.DatabaseHelperImpl
import com.mahmouddev.appweather.util.Constants
import com.mahmouddev.appweather.util.Constants.LATITUDE
import com.mahmouddev.appweather.util.Constants.LONGITUDE
import com.mahmouddev.appweather.util.Helper
import com.mahmouddev.appweather.util.Helper.kelvinToCelsius
import com.mahmouddev.appweather.util.Helper.kelvinToFahrenheit
import com.mahmouddev.appweather.util.MyPreferences
import com.mahmouddev.appweather.util.NotificationHelper
import com.mahmouddev.appweather.util.ViewHelper.gone
import com.mahmouddev.appweather.util.ViewHelper.visible
import com.mahmouddev.appweather.viewModel.WeatherViewModel

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var viewModel: WeatherViewModel
    private lateinit var locationHelper: LocationHelper
    private var lng: Double? = 0.0
    private var lat: Double? = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MyPreferences.context = this
        initGpsLocation()
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        setupBottomBar()
        initViewModel()
        locationHelper.checkLocationPermissions()


        setupObserverCurrentUserWeather()


        navController.addOnDestinationChangedListener { _, destination, _ ->  //3
            if (destination.id in arrayOf(R.id.navSettingsFragment)) {
                binding.tvCurrentCityWeather.gone()
            } else
                binding.tvCurrentCityWeather.visible()

        }

        NotificationHelper(this).createNotification("ttt","ddd")

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


    @SuppressLint("SetTextI18n")
    private fun setupObserverCurrentUserWeather() {

        viewModel.getCurrentUserWeather().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { data ->
                            binding.tvCurrentCityWeather.text = "Current user weather: \n" +
                                    "city: ${data.city},  humidity: ${data.humidity}\n" +
                                    "wind speed: ${data.windSpeed},  pressure: ${data.pressure} \n" +
                                    "Temp: ${Helper.handleTemp(this,data.temp)}"

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

    private fun initGpsLocation() {
        locationHelper = LocationHelper(this, object : LocationManager {

            override fun onLocationChanged(location: Location?) {
                Log.e(TAG, "onLocationChanged latitude: ${location?.latitude}")
                Log.e(TAG, "onLocationChanged longitude: ${location?.longitude}")
            }

            override fun getLastKnownLocation(location: Location?) {
                lat = location?.latitude
                lng = location?.longitude

                Log.e(TAG, "getLastKnownLocation latitude: ${location?.latitude}")
                Log.e(TAG, "getLastKnownLocation longitude: ${location?.longitude}")
                if (lat != null && lng != null) {
                    viewModel.fetchCurrentUserWeather(lat!!, lng!!)
                    MyPreferences.setFloat(LATITUDE, lat!!.toFloat())
                    MyPreferences.setFloat(LONGITUDE, lng!!.toFloat())
                }
            }

        })

    }

    override fun onResume() {
        super.onResume()

        if (locationHelper.checkLocationPermissions()) {
            if (locationHelper.checkMapServices()) {
                locationHelper.startLocationUpdates()

            }
        }
    }

    override fun onStop() {
        super.onStop()
        locationHelper.stopLocationUpdates()

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navSettingsFragment -> {
                val action = NavGraghDirections.actionGlobalNavSettingsFragment()
                navController.navigate(action)

            }
        }
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }



}