package com.example.showphoto

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PhotoViewModel(app: Application): AndroidViewModel(app) {
    private val internal_photos = MutableLiveData<List<Photo>>()
    init {
        internal_photos.value = listOf()
        loadPhotos()
    }
    val photos: LiveData<List<Photo>> = internal_photos

    /**
     *  Load a list of country.
     *  Fetch from local first. If data is not available locally, fetch them from server.
     * */
    fun loadPhotos() {
        viewModelScope.launch {
            PhotoFetchService.get().fetchCountryList(object :
                PhotoFetchCallback<List<Photo>> {
                override fun onSuccess(data: List<Photo>) {
                    internal_photos.value = data
                }

                override fun onFailure(error: String) {
                    Log.d("tag", "PhotoFetchCallback onFailure: $error")
                }
            })
        }
    }
}