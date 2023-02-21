package com.example.weatherapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.data.CurrentWeather
import com.example.weatherapp.data.ForecastWeather
import com.example.weatherapp.data.ForecastWeatherEntity
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.data.Constants.ICON_PREFIX

class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Load current location's weather
                val locationManager =
                    getSystemService(Context.LOCATION_SERVICE) as LocationManager
                viewModel
                    .fetchWeatherDataWithCurrentLocation(locationManager, this)
            }
        }
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: WeatherViewModel
    private lateinit var adapter: WeatherForecastAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "onCreate")
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        setupRecyclerViewAndObserve()

        binding.buttonSearch.setOnClickListener {
            val userInput = binding.textInput.text.toString()
            Log.d(tag, "button clicked, search for $userInput")
            it.isEnabled = false
            viewModel.fetchCurrentWeather(userInput)
            viewModel.fetchForecastWeather(userInput)
        }

        checkLocationPermission()
    }

    /**
     *  Check if location permission is granted.
     *  If not granted, request permission from user.
     * **/
    fun checkLocationPermission() {
           if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
               Log.d(tag, "location permission not granted, ask user.")
               requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
    }

    /**
     *  Set up recyclerView and start observing
     *  current weather and weather forecast live data.
     * **/
    fun setupRecyclerViewAndObserve() {
        val layoutManager = LinearLayoutManager(this)
        this.binding.recyclerview.layoutManager = layoutManager
        this.adapter = WeatherForecastAdapter(mutableListOf<ForecastWeatherEntity>())
        this.binding.recyclerview.adapter = this.adapter

        viewModel.currentWeather.observe(this, object : Observer<CurrentWeather?> {
            override fun onChanged(weather: CurrentWeather?) {
                Log.d(tag, "currentWeather onChanged")
                if (weather != null) {
                    binding.setCurWeather(weather)
                    val idt = resources.getIdentifier(ICON_PREFIX+weather.icon, null, packageName)
                    binding.conditionIcon.setImageResource(idt)
                }else {
                    Log.d(tag, "currentWeather is null")
                    binding.setCurWeather(null)
                    binding.curWeather.setText(getString(R.string.no_data))
                    binding.conditionIcon.setImageResource(0)
                }
                binding.buttonSearch.isEnabled = true
            }
        })
        viewModel.weatherForecast.observe(this, object : Observer<ForecastWeather?> {
            override fun onChanged(data: ForecastWeather?) {
                Log.d(tag, "weatherForecast onChanged")
                adapter.getDataList().clear()
                if (data != null) {
                    adapter.getDataList().addAll(data.list)
                }
                adapter.notifyDataSetChanged()
                binding.buttonSearch.isEnabled = true
            }
        })
    }
}