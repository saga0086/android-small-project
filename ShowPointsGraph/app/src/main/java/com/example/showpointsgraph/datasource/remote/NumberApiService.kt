package com.example.showpointsgraph.datasource.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface NumberApiService {
    @GET("integers")
    fun getData(
        @Query("num") num: Int,
        @Query("min") min: Int,
        @Query("max") max: Int,
        @Query("col") col: Int,
        @Query("base") base: Int,
        @Query("format") format: String,
        @Query("rnd") rnd: String
    ): Call<String>
}