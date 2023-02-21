package com.example.weatherapp.data

data class ForecastWeather(val list: List<ForecastWeatherEntity>,
                           val cityName: String, val country: String)

data class ForecastWeatherEntity(val timeStamp: String, val weatherName: String,
                                 val icon: String, val temp: Double,
                                 val feels_like: Double)