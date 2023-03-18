package com.example.showcountries.datasource.remote

import android.util.Log
import com.example.showcountries.data.Constants.BASE_URL_COUNTRY
import com.example.showcountries.data.CountryResponseData

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CountryFetchService private constructor(){
    companion object {
        private var instance: CountryFetchService? = null
            get() {
                if (field == null) {
                    field = CountryFetchService()
                }
                return field
            }
        fun get(): CountryFetchService{
            return instance!!
        }
    }
    val tag = "CountryFetchService"
    val okHttpClient: OkHttpClient = OkHttpClient()
    var retrofit: Retrofit = Retrofit.Builder().client(okHttpClient)
        .baseUrl(BASE_URL_COUNTRY)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     *  Fetch a list of country detail from server.
     *  @param listener the callback function
     * */
    fun fetchCountryList(listener: CountryFetchCallback<List<CountryResponseData>>) {
        val apiService: CountryFetchApiService = this.retrofit.create(CountryFetchApiService::class.java)
        val callable: Call<List<CountryResponseData>> = apiService.getCountryList()
        Log.d(tag, "start fetching list of countries")
        callable.enqueue(object : Callback<List<CountryResponseData>> {
            override fun onResponse(call: Call<List<CountryResponseData>>?, response: Response<List<CountryResponseData>>) {
                Log.d(tag, "got response")
                val data = response.body();
                data?.let {
                    listener.onSuccess(it)
                } ?: kotlin.run {
                    listener.onFailure("returned data is null!")
                }
            }
            override fun onFailure(call: Call<List<CountryResponseData>>?, t: Throwable) {
                Log.d(tag, "got failure", t)
                listener.onFailure(t.message ?: "got error")
            }
        })
    }
}