package com.example.showphoto

import android.util.Log

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
class PhotoFetchService private constructor(){
    companion object {
        private var instance: PhotoFetchService? = null
            get() {
                if (field == null) {
                    field = PhotoFetchService()
                }
                return field
            }
        fun get(): PhotoFetchService{
            return instance!!
        }
    }
    val tag = "CountryFetchService"
    val okHttpClient: OkHttpClient = OkHttpClient()
    var retrofit: Retrofit = Retrofit.Builder().client(okHttpClient)
        .baseUrl("https://api.slingacademy.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     *  Fetch a list of country detail from server.
     *  @param listener the callback function
     * */
    fun fetchCountryList(listener: PhotoFetchCallback<List<Photo>>) {
        val apiService: ApiServices = this.retrofit.create(ApiServices::class.java)
        val callable: Call<PhotoResponseData> = apiService.getPhotoList()
        callable.enqueue(object : Callback<PhotoResponseData> {
            override fun onResponse(call: Call<PhotoResponseData>?, response: Response<PhotoResponseData>) {
                Log.d(tag, "got response")
                val data = response.body();
                data?.let {
                    listener.onSuccess(it.photos)
                } ?: kotlin.run {
                    listener.onFailure("returned data is null!")
                }
            }
            override fun onFailure(call: Call<PhotoResponseData>?, t: Throwable) {
                Log.d(tag, "got failure", t)
                listener.onFailure(t.message ?: "got error")
            }
        })
    }
}