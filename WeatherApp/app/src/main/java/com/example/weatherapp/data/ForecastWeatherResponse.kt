package com.example.weatherapp.data

data class ForecastWeatherResponse(val list: List<ForecastWeatherResponseEntity>,
                                   val city: ForecastWeatherResponseCity)

data class ForecastWeatherResponseEntity(val dt: Long, val main: ForecastWeatherResponseTemperature,
                                         val weather: List<ForecastWeatherResponseWeather>)

data class ForecastWeatherResponseWeather(val id: Int, val main: String,
                                         val description: String, val icon: String)

data class ForecastWeatherResponseTemperature(val temp: Double, val feels_like: Double)

data class ForecastWeatherResponseCity(val id: Int, val name: String, val country: String)
