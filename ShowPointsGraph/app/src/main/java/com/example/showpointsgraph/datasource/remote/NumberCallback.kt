package com.example.showpointsgraph.datasource.remote

interface NumberCallback {
    fun onSuccess(number: String?)

    fun onFail(t: Throwable?)
}