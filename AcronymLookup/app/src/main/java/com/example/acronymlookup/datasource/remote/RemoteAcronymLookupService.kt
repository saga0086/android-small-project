/*
 * Copyright 2023
 * Author: Wei
 */

package com.example.acronymlookup.datasource.remote

import com.example.acronymlookup.datamodel.LookupData
import com.example.acronymlookup.datasource.AcronymLookupService
import com.example.acronymlookup.datasource.LookupListener
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteAcronymLookupService : AcronymLookupService {
    val BASE_URL = "http://www.nactem.ac.uk"

    override fun lookup(acronym: String, listener: LookupListener<List<LookupData>>) {
        val okHttpClient = OkHttpClient()
        val retrofit: Retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService: LookupApiService = retrofit.create(LookupApiService::class.java)
        val callable: Call<List<LookupData>> = apiService.getFullForm(acronym)
        callable.enqueue(object : Callback<List<LookupData>> {
            override fun onResponse(call: Call<List<LookupData>>?, response: Response<List<LookupData>>) {
                val data = response.body();
                if (data != null) {
                    listener.onSuccess(data)
                } else{
                    listener.onFailure("returned data is null!")
                }
            }

            override fun onFailure(call: Call<List<LookupData>>?, t: Throwable) {
                listener.onFailure(t.message ?: "got error")
            }
        })
    }
}