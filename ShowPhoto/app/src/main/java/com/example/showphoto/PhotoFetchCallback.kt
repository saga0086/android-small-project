package com.example.showphoto

interface PhotoFetchCallback<T> {
    fun onSuccess(data: T)

    fun onFailure(error: String)
}