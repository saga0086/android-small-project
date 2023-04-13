package com.example.showpointsgraph.datasource.remote

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NumberFetchService {
    val tag = "NumberFetchService"
    val retrofit = RetrofitClient.getInstance()

    fun fetchNumberPlain(numberCallback: NumberCallback) {
        Log.d(tag, "calling fetchNumberPlain")
        val myInterface: NumberApiService = retrofit.create(NumberApiService::class.java)
        val call: Call<String> = myInterface.getData(1, 1, 100,
            1, 10, "plain", "new")
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String?>?) {
                Log.d(tag, "onSuccess")
                response?.let {
                    val number: String? = it.body()
                    numberCallback.onSuccess(number)
                } ?: kotlin.run {
                    numberCallback.onFail(java.lang.Exception("response is null"))
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable?) {
                Log.d(tag, "onFailure")
                numberCallback.onFail(t)
            }
        })
    }
}