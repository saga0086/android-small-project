package com.example.weatherapp.datasource.remote

import android.util.Log
import com.example.weatherapp.data.Constants.API_KEY
import com.example.weatherapp.data.Constants.BASE_URL_WEATHER
import com.example.weatherapp.data.CurrentWeatherResponse
import com.example.weatherapp.data.ForecastWeatherResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherFetchService {
    companion object {
        private var instance: WeatherFetchService? = null
            get() {
                if (field == null) {
                    field = WeatherFetchService()
                }
                return field
            }
        fun get(): WeatherFetchService{
            return instance!!
        }
    }
    val tag = "WeatherFetchService"
    val okHttpClient: OkHttpClient = OkHttpClient()
    var retrofit: Retrofit = Retrofit.Builder().client(okHttpClient)
        .baseUrl(BASE_URL_WEATHER)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     *  Fetch current weather data from server by city name.
     *  @param cityName name of the city
     *  @param listener the callback function
     * */
    fun fetchCurrentWeather(cityName: String, listener: WeatherFetchCallback<CurrentWeatherResponse>) {
        val apiService: WeatherApiService = this.retrofit.create(WeatherApiService::class.java)
        val callable: Call<CurrentWeatherResponse> = apiService.getCurrentWeather(cityName, API_KEY)
        Log.d(tag, "start fetching current weather at $cityName")
        callable.enqueue(object : Callback<CurrentWeatherResponse> {
            override fun onResponse(call: Call<CurrentWeatherResponse>?, response: Response<CurrentWeatherResponse>) {
                Log.d(tag, "got response")
                val data = response.body();
                if (data != null) {
                    listener.onSuccess(data)
                } else{
                    listener.onFailure("returned data is null!")
                }
            }
            override fun onFailure(call: Call<CurrentWeatherResponse>?, t: Throwable) {
                Log.d(tag, "got failure", t)
                listener.onFailure(t.message ?: "got error")
            }
        })
    }

    /**
     *  Fetch weather forecast data from server by city name.
     *  @param cityName name of the city
     *  @param listener the callback function
     * */
    fun fetchForecastWeather(cityName: String, listener: WeatherFetchCallback<ForecastWeatherResponse>) {
        val apiService: WeatherApiService = this.retrofit.create(WeatherApiService::class.java)
        val callable: Call<ForecastWeatherResponse> = apiService.getForecastWeather(cityName, API_KEY)
        Log.d(tag, "start fetching weather forecast at $cityName")
        callable.enqueue(object : Callback<ForecastWeatherResponse> {
            override fun onResponse(call: Call<ForecastWeatherResponse>?, response: Response<ForecastWeatherResponse>) {
                Log.d(tag, "got response")
                val data = response.body()
                if (data != null) {
                    listener.onSuccess(data)
                } else{
                    listener.onFailure("returned data is null!")
                }
            }
            override fun onFailure(call: Call<ForecastWeatherResponse>?, t: Throwable) {
                Log.d(tag, "got failure", t)
                listener.onFailure(t.message ?: "got error")
            }
        })
    }

    /**
     *  Fetch current weather data from server by coord.
     *  @param lat latitude of the place
     *  @param lon longitude of the place
     *  @param listener the callback function
     * */
    fun fetchCurrentWeather(lat: Double, lon: Double, listener: WeatherFetchCallback<CurrentWeatherResponse>) {
        val apiService: WeatherApiService = this.retrofit.create(WeatherApiService::class.java)
        val callable: Call<CurrentWeatherResponse> = apiService.getCurrentWeather(lat, lon, API_KEY)
        Log.d(tag, "start fetching current weather at $lat , $lon")
        callable.enqueue(object : Callback<CurrentWeatherResponse> {
            override fun onResponse(call: Call<CurrentWeatherResponse>?, response: Response<CurrentWeatherResponse>) {
                Log.d(tag, "got response")
                val data = response.body();
                if (data != null) {
                    listener.onSuccess(data)
                } else{
                    listener.onFailure("returned data is null!")
                }
            }
            override fun onFailure(call: Call<CurrentWeatherResponse>?, t: Throwable) {
                Log.d(tag, "got failure", t)
                listener.onFailure(t.message ?: "got error")
            }
        })
    }

    /**
     *  Fetch forecast weather data from server by coord.
     *  @param lat latitude of the place
     *  @param lon longitude of the place
     *  @param listener the callback function
     * */
    fun fetchForecastWeather(lat: Double, lon: Double, listener: WeatherFetchCallback<ForecastWeatherResponse>) {
        val apiService: WeatherApiService = this.retrofit.create(WeatherApiService::class.java)
        val callable: Call<ForecastWeatherResponse> = apiService.getForecastWeather(lat, lon, API_KEY)
        Log.d(tag, "start fetching weather forecast at $lat , $lon")
        callable.enqueue(object : Callback<ForecastWeatherResponse> {
            override fun onResponse(call: Call<ForecastWeatherResponse>?, response: Response<ForecastWeatherResponse>) {
                Log.d(tag, "got response")
                val data = response.body()
                if (data != null) {
                    listener.onSuccess(data)
                } else{
                    listener.onFailure("returned data is null!")
                }
            }
            override fun onFailure(call: Call<ForecastWeatherResponse>?, t: Throwable) {
                Log.d(tag, "got failure", t)
                listener.onFailure(t.message ?: "got error")
            }
        })
    }
}