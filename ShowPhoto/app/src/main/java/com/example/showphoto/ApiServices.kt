package com.example.showphoto

import retrofit2.Call
import retrofit2.http.GET

interface ApiServices {
    @GET("v1/sample-data/photos")
    fun getPhotoList(): Call<PhotoResponseData>
}