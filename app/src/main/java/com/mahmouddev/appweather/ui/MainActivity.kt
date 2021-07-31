package com.mahmouddev.appweather.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.android.talabaty.util.LocationManager
import com.mahmouddev.appweather.NavGraghDirections
import com.mahmouddev.appweather.R
import com.mahmouddev.appweather.databinding.ActivityMainBinding
import com.mahmouddev.appweather.util.dbUtil.Status
import com.mahmouddev.appweather.location.LocationHelper
import com.mahmouddev.appweather.room.entity.WeatherCity
import com.mahmouddev.appweather.util.*
import com.mahmouddev.appweather.util.Constants.LATITUDE
import com.mahmouddev.appweather.util.Constants.LONGITUDE
import com.mahmouddev.appweather.util.ViewHelper.gone
import com.mahmouddev.appweather.util.ViewHelper.visible
import com.mahmouddev.appweather.viewModel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlin.math.ln

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    private val viewModel : WeatherViewModel by viewModels()
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var locationHelper: LocationHelper
    private lateinit var cities :List<WeatherCity>
    private var lng: Double? = 0.0
    private var lat: Double? = 0.0
    private var isExported = false
    var onPermissionGrant: ((Double,Double) -> Unit)? = null


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
        locationHelper.checkLocationPermissions()
        viewModel.fetchFavWeather()
        observeFavCities()

        setupObserverCurrentUserWeather()


        navController.addOnDestinationChangedListener { _, destination, _ ->  //3
            if (destination.id in arrayOf(R.id.navSettingsFragment)) {
                binding.tvCurrentCityWeather.gone()
            } else
                binding.tvCurrentCityWeather.visible()

        }
        AlarmHelper.startAlarm(this)

    }

    private fun setupBottomBar() {
        binding.bottomNavigation.setupWithNavController(navController)
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
                                    "Temp: ${Helper.handleTemp(this, data.temp)}"

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
            }

            override fun getLastKnownLocation(location: Location?) {
                lat = location?.latitude
                lng = location?.longitude

                Log.e(TAG, "getLastKnownLocation latitude: ${location?.latitude}")
                Log.e(TAG, "getLastKnownLocation longitude: ${location?.longitude}")
                if (lat != null && lng != null) {
                    onPermissionGrant?.invoke(lat!!, lng!!)
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
            R.id.csvItem -> {

                GlobalScope.launch(Dispatchers.Main) {
                    isExported =true
                    viewModel.fetchFavWeather()

                }



            }
        }
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    Log.d("focus", "touchevent")
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }


    private fun observeFavCities(){
        viewModel.getFavoriteWeather().observe(this,
            Observer {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let { data ->
                            Log.e(TAG, "observeFavCities: $data", )
                            cities = data
                            if (isExported){
                                CSVHelper.exportDatabaseToCSVFile(this@MainActivity,cities)
                                isExported= false
                            }

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