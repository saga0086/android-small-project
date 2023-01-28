/*
 * Copyright 2023
 * Author: Wei
 */

package com.example.acronymlookup.datasource.remote

import com.example.acronymlookup.datamodel.LookupData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LookupApiService {
    @GET("software/acromine/dictionary.py")
    fun getFullForm(@Query("sf") sf: String): Call<List<LookupData>>
}