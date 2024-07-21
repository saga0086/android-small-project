package com.example.showwmtproduct.datasource.remote

import com.example.showwmtproduct.data.ProductResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductFetchApiService {
    @GET("v1/sample-data/photos")
    fun getProductList(): Call<ProductResponseData>

    @GET("v1/sample-data/photos")
    fun getProductListByPage(@Query("offset") offset: Int, @Query("limit") limit: Int): Call<ProductResponseData>
}