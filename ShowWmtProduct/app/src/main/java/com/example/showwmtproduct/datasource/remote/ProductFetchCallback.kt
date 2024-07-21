package com.example.showwmtproduct.datasource.remote

interface ProductFetchCallback<T> {
    fun onSuccess(data: T)

    fun onFailure(error: String)
}