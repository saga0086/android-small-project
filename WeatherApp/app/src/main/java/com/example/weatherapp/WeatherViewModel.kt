package com.example.weatherapp

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.*
import com.example.weatherapp.datasource.remote.WeatherFetchCallback
import com.example.weatherapp.datasource.remote.WeatherFetchService
import com.example.weatherapp.util.WeatherDataTransfer
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    val currentWeather = MutableLiveData<CurrentWeather?>()
    val weatherForecast = MutableLiveData<ForecastWeather?>()
    val tag = "WeatherViewModel"
    init {
        viewModelScope.launch {
            loadLastWeatherData()
        }
    }

    /**
     *  Fetch current weather from server by city name
     *  @param city the name of the city to fetch
     */
    fun fetchCurrentWeather(city: String) {
        val service = WeatherFetchService.get()
        Log.d(tag, "fetchCurrentWeather start")
        service.fetchCurrentWeather(city, object : WeatherFetchCallback<CurrentWeatherResponse> {
            override fun onSuccess(data: CurrentWeatherResponse) {
                Log.d(tag, "success")
                currentWeather.value = WeatherDataTransfer.get()
                    .convertCurrentWeatherResponse(data)
                storeCurrentWeather()
            }

            override fun onFailure(error: String) {
                Log.d(tag, "onFailure: $error")
                currentWeather.value = null
            }
        })
    }

    /**
     *  Fetch weather forecast from server by city name
     *  @param city the name of the city to fetch
     */
    fun fetchForecastWeather(city: String) {
        val service = WeatherFetchService.get()
        Log.d(tag, "fetchForecastWeather start")
        service.fetchForecastWeather(city, object : WeatherFetchCallback<ForecastWeatherResponse> {
            override fun onSuccess(data: ForecastWeatherResponse) {
                Log.d(tag, "success")
                weatherForecast.value = WeatherDataTransfer.get()
                    .convertForecastWeatherResponse(data)
                storeForecastWeather()
            }

            override fun onFailure(error: String) {
                Log.d(tag, "onFailure: $error")
                weatherForecast.value = null
            }
        })
    }

    /**
     *  Fetch weather forecast from server by coord
     *  @param lat the latitude
     *  @param lon the longitude
     */
    fun fetchForecastWeather(lat: Double, lon: Double) {
        val service = WeatherFetchService.get()
        Log.d(tag, "fetchForecastWeather by coord start")
        service.fetchForecastWeather(lat, lon, object : WeatherFetchCallback<ForecastWeatherResponse> {
            override fun onSuccess(data: ForecastWeatherResponse) {
                Log.d(tag, "success")
                weatherForecast.value = WeatherDataTransfer.get()
                    .convertForecastWeatherResponse(data)
                storeForecastWeather()
            }

            override fun onFailure(error: String) {
                Log.d(tag, "onFailure: $error")
                weatherForecast.value = null
            }
        })
    }

    /**
     *  Fetch current weather from server by coord
     *  @param lat the latitude
     *  @param lon the longitude
     */
    fun fetchCurrentWeather(lat: Double, lon: Double) {
        val service = WeatherFetchService.get()
        Log.d(tag, "fetchCurrentWeather by coord start")
        service.fetchCurrentWeather(lat, lon, object : WeatherFetchCallback<CurrentWeatherResponse> {
            override fun onSuccess(data: CurrentWeatherResponse) {
                Log.d(tag, "success")
                currentWeather.value = WeatherDataTransfer.get()
                    .convertCurrentWeatherResponse(data)
                storeCurrentWeather()
            }

            override fun onFailure(error: String) {
                Log.d(tag, "onFailure: $error")
                currentWeather.value = null
            }
        })
    }

    /**
     *  Load last searched weather data from local storage
     *  and update live data.
     */
    fun loadLastWeatherData() {
        // For simplicity just use sharedPreference to store the last weather data.
        // When there are more weather data to persist, can replace this with database(Room).
        val sharedPref = getApplication<Application>().getSharedPreferences(Constants.SHARED_PREF_WEATHER, Context.MODE_PRIVATE)
        var data = sharedPref.getString(Constants.SHARED_PREF_KEY_CUR_WEATHER, "")
        if (!data.isNullOrEmpty()){
            currentWeather.value = WeatherDataTransfer.get().convertJsonStringToCurrentWeather(data)
        }else{
            Log.d(tag, "last current weather is empty")
            currentWeather.value = null
        }
        data = sharedPref.getString(Constants.SHARED_PREF_KEY_FORECAST_WEATHER, "")
        if (!data.isNullOrEmpty()){
            weatherForecast.value = WeatherDataTransfer.get().convertJsonStringToForecastWeather(data)
        }else{
            Log.d(tag, "last weather forecast is empty")
            weatherForecast.value = null
        }
    }

    /**
     *  Store current weather data into local storage
     */
    fun storeCurrentWeather(){
        if (currentWeather.value != null) {
            val sharedPref = getApplication<Application>().getSharedPreferences(Constants.SHARED_PREF_WEATHER, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString(Constants.SHARED_PREF_KEY_CUR_WEATHER,
                WeatherDataTransfer.get().convertCurrentWeatherToJsonString(currentWeather.value!!))
            editor.apply()
            Log.d(tag, "current weather saved.")
        }
    }

    /**
     *  Store forecast weather data into local storage
     */
    fun storeForecastWeather(){
        if (weatherForecast.value != null) {
            val sharedPref = getApplication<Application>().getSharedPreferences(Constants.SHARED_PREF_WEATHER, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString(Constants.SHARED_PREF_KEY_FORECAST_WEATHER,
                WeatherDataTransfer.get().convertForecastWeatherToJsonString(weatherForecast.value!!))
            editor.apply()
            Log.d(tag, "forecast weather saved.")
        }
    }
}