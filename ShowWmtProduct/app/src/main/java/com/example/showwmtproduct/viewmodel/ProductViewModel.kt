package com.example.showwmtproduct.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.showwmtproduct.data.Constants.LIMIT_PER_PAGE
import com.example.showwmtproduct.data.Product
import com.example.showwmtproduct.data.ProductResponseData
import com.example.showwmtproduct.datasource.remote.ProductFetchCallback
import com.example.showwmtproduct.datasource.remote.ProductFetchService
import kotlinx.coroutines.launch

class ProductViewModel(app: Application): AndroidViewModel(app) {
    private val tag = "ProductViewModel"
    private var isLoading = false
    private var productSize = 0
    private val internal_products = mutableListOf<Product>()
    val products = MutableLiveData<List<Product>?>()
    init {
        //loadProductList()
    }

    fun isLoading(): Boolean {
        return isLoading
    }

    /**
     *  Load a list of Products and update LiveData and total product count.
     * */
    fun loadProductList() {
        viewModelScope.launch {
            Log.i(tag, "loading Product List.")
            isLoading = true
            ProductFetchService.get()
                .fetchProductList(object : ProductFetchCallback<ProductResponseData> {
                    override fun onSuccess(data: ProductResponseData) {
                        Log.d(tag, "loadProductList: ProductFetchCallback onSuccess")
                        isLoading = false
                        internal_products.clear()
                        internal_products.addAll(data.photos)
                        products.value = internal_products
                        productSize = data.total_photos
                    }

                    override fun onFailure(error: String) {
                        Log.d(tag, "loadProductList: ProductFetchCallback onFailure: $error")
                        isLoading = false
                        products.value = listOf(getError(error))
                        productSize = 0
                        internal_products.clear()
                    }
                })
        }
    }

    /**
     *  Add Products and append to the list by offset.
     *  @param offset the offset
     * */
    fun addProductListByOffset(offset: Int) {
        viewModelScope.launch {
            Log.d(tag, "Adding Products by offset: $offset")
            isLoading = true
            ProductFetchService.get()
                .fetchProductListByPage(object : ProductFetchCallback<ProductResponseData> {
                    override fun onSuccess(data: ProductResponseData) {
                        Log.d(tag, "addProductListByOffset: ProductFetchCallback onSuccess")
                        isLoading = false
                        internal_products.addAll(data.photos)
                        products.value = internal_products
                    }

                    override fun onFailure(error: String) {
                        Log.d(tag, "addProductListByOffset: ProductFetchCallback onFailure: $error")
                        isLoading = false
                    }
                }, offset, LIMIT_PER_PAGE)
        }
    }

    /**
     *  Load a list of Products by page.
     *  @param pageNumber the page number
     * */
    fun loadProductListByPage(pageNumber: Int) {
        viewModelScope.launch {
            Log.i(tag, "loading Product List by page.")
            ProductFetchService.get()
                .fetchProductListByPage(object : ProductFetchCallback<ProductResponseData> {
                    override fun onSuccess(data: ProductResponseData) {
                        Log.d(tag, "loadProductListByPage: ProductFetchCallback onSuccess")
                        internal_products.clear()
                        internal_products.addAll(data.photos)
                        products.value = internal_products
                    }

                    override fun onFailure(error: String) {
                        Log.d(tag, "loadProductListByPage: ProductFetchCallback onFailure: $error")
                        products.value = mutableListOf(getError(error))
                        internal_products.clear()
                    }
                }, getOffsetByPageNumber(pageNumber), LIMIT_PER_PAGE)
        }
    }

    /**
     *  Return if there is more product to load from server.
     *  @return true is there is more product, false otherwise
     * */
    fun hasMoreProduct(): Boolean{
        return productSize > (products.value?.size ?: 0)
    }

    /**
     *  Get a product with error
     *  @param error the error message
     *  @return the product with error
     * */
    fun getError(error: String): Product{
        return Product("", error, -1, "")
    }

    /**
     *  Calculate the offset by page number.
     *  @param pageNumber the page number
     *  @return the offset
     * */
    fun getOffsetByPageNumber(pageNumber: Int): Int{
        return (pageNumber - 1) * LIMIT_PER_PAGE
    }

    fun getData(): List<Product> {
        val list = mutableListOf<Product>()
        list.addAll(internal_products)
        return list
    }

    fun getDataSize() = internal_products.size
}