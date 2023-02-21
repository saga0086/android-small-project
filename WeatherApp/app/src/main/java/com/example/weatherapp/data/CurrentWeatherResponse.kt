package com.example.weatherapp.data

data class CurrentWeatherResponse(val weather: List<CurrentWeatherResponseWeather>,
                                  val main: CurrentWeatherResponseTemperature, val dt: Long,
                                  val sys: CurrentWeatherResponseSys, val name: String)

data class CurrentWeatherResponseWeather(val id: Int, val main: String,
                                             val description: String, val icon: String)

data class CurrentWeatherResponseTemperature(val temp: Double, val feels_like: Double)

data class CurrentWeatherResponseSys(val id: Int, val country: String)
