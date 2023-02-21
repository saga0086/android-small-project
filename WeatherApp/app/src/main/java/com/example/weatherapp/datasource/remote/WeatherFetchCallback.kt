package com.example.weatherapp.datasource.remote

interface WeatherFetchCallback<T> {
    fun onSuccess(data: T)
    fun onFailure(error: String)
}