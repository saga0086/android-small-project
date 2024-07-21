package com.example.showwmtproduct.datasource.remote

import android.util.Log
import com.example.showwmtproduct.data.Constants.BASE_URL_PRODUCT
import com.example.showwmtproduct.data.ProductResponseData
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductFetchService private constructor(){
    companion object {
        private var instance: ProductFetchService? = null
            get() {
                if (field == null) {
                    field = ProductFetchService()
                }
                return field
            }
        fun get(): ProductFetchService{
            return instance!!
        }
    }
    val tag = "ProductFetchService"
    val okHttpClient: OkHttpClient = OkHttpClient()
    val retrofit: Retrofit = Retrofit.Builder().client(okHttpClient)
        .baseUrl(BASE_URL_PRODUCT)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     *  Fetch a list of products from server.
     *  @param listener the callback function
     * */
    fun fetchProductList(listener: ProductFetchCallback<ProductResponseData>) {
        val apiService: ProductFetchApiService = this.retrofit.create(ProductFetchApiService::class.java)
        val callable: Call<ProductResponseData> = apiService.getProductList()
        Log.d(tag, "start fetching list of products")
        callable.enqueue(object : Callback<ProductResponseData> {
            override fun onResponse(call: Call<ProductResponseData>?, response: Response<ProductResponseData>) {
                Log.d(tag, "got response")
                val data = response.body();
                data?.let {
                    listener.onSuccess(it)
                } ?: kotlin.run {
                    listener.onFailure("returned data is null! status code: ${response.code()}, message: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ProductResponseData>?, t: Throwable) {
                Log.d(tag, "got failure", t)
                listener.onFailure(t.message ?: "got error")
            }
        })
    }

    /**
     *  Fetch a list of products by page number and limit from server.
     *  @param listener the callback function
     *  @param offset the offset from the list
     *  @param limit maximum number of products per page
     * */
    fun fetchProductListByPage(listener: ProductFetchCallback<ProductResponseData>, offset: Int, limit: Int) {
        val apiService: ProductFetchApiService = this.retrofit.create(ProductFetchApiService::class.java)
        val callable: Call<ProductResponseData> = apiService.getProductListByPage(offset, limit)
        Log.d(tag, "start fetching list of products by page and limit.")
        callable.enqueue(object : Callback<ProductResponseData> {
            override fun onResponse(call: Call<ProductResponseData>?, response: Response<ProductResponseData>) {
                Log.d(tag, "got response")
                val data = response.body();
                data?.let {
                    listener.onSuccess(it)
                } ?: kotlin.run {
                    listener.onFailure("returned data is null! status code: ${response.code()}, message: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ProductResponseData>?, t: Throwable) {
                Log.d(tag, "got failure", t)
                listener.onFailure(t.message ?: "got error")
            }
        })
    }
}