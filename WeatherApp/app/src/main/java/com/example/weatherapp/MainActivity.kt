package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
            Log.d(tag, "button clicked, search for ")
            val userInput = binding.textInput.text.toString()
            it.isEnabled = false
            viewModel.fetchCurrentWeather(userInput)
            viewModel.fetchForecastWeather(userInput)
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