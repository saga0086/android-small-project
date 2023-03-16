package com.example.showcountries.datasource.remote

interface CountryFetchCallback<T> {

    fun onSuccess(data: T)

    fun onFailure(error: String)
}