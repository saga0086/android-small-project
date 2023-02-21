package com.example.weatherapp.data

data class CurrentWeather(val cityName: String, val country: String,
                          val timeStamp: String, val weatherName: String,
                          val icon: String, val temp: Double,
                          val feels_like: Double)
