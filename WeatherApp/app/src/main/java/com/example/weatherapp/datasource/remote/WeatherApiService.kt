package com.example.weatherapp.datasource.remote

import com.example.weatherapp.data.CurrentWeatherResponse
import com.example.weatherapp.data.ForecastWeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("data/2.5/weather")
    fun getCurrentWeather(@Query("q") city: String, @Query("appid") apiKey: String, @Query("units") units: String = "imperial"): Call<CurrentWeatherResponse>

    @GET("data/2.5/forecast")
    fun getForecastWeather(@Query("q") city: String, @Query("appid") apiKey: String, @Query("units") units: String = "imperial"): Call<ForecastWeatherResponse>

    @GET("data/2.5/weather")
    fun getCurrentWeather(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appid") apiKey: String, @Query("units") units: String = "imperial"): Call<CurrentWeatherResponse>

    @GET("data/2.5/forecast")
    fun getForecastWeather(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appid") apiKey: String, @Query("units") units: String = "imperial"): Call<ForecastWeatherResponse>
}