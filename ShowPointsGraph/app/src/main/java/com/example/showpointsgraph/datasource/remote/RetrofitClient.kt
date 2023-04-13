package com.example.showpointsgraph.datasource.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {

    const val BASE_URL = "https://www.random.org"
    fun getInstance(): Retrofit {

        var httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        var okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build()
        return retrofit
    }
}