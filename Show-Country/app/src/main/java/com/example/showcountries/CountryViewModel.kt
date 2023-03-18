package com.example.showcountries

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.showcountries.data.Country
import com.example.showcountries.data.CountryResponseData
import com.example.showcountries.datasource.local.CountryRepo
import com.example.showcountries.datasource.remote.CountryFetchCallback
import com.example.showcountries.datasource.remote.CountryFetchService
import com.example.showcountries.util.CountryDataTransfer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CountryViewModel(val app: Application) : AndroidViewModel(app) {
    val tag = "CountryViewModel"
    val headline = MutableLiveData<String?>()
    val countries = MutableLiveData<List<Country>?>()
    init {
        loadCountryList()
    }

    /**
     *  Load a list of country.
     *  Fetch from local first. If data is not available locally, fetch them from server.
     * */
    fun loadCountryList() {
        headline.value = app.getString(R.string.title_loading_country)
        viewModelScope.launch {
            Log.d(tag, "loading country list")
            val data = withContext(Dispatchers.IO) {
                fetchFromLocal()
            }
            data?.let {
                Log.d(tag, "data fetched from local.")
                headline.value = app.getString(R.string.tile_country_list)
                countries.value = it
            } ?: kotlin.run {
                Log.d(tag, "Local data not available, fetch from server.")
                fetchFromServer()
            }
        }
    }

    /**
     *  Load a list of country from local
     * */
    fun fetchFromLocal(): List<Country>? {
        return CountryRepo.get().getCountryList(app)
    }

    /**
     *  Load a list of country from server
     *  and then update the live data and store into local file.
     * */
    fun fetchFromServer() {
        CountryFetchService.get().fetchCountryList(object :
            CountryFetchCallback<List<CountryResponseData>> {
            override fun onSuccess(data: List<CountryResponseData>) {
                Log.d(tag, "CountryFetchCallback onSuccess: ${data.size}")
                headline.value = app.getString(R.string.tile_country_list)
                val countryList = CountryDataTransfer.getListOfCountry(data)
                countries.value = countryList
                viewModelScope.launch(Dispatchers.IO) {
                    CountryRepo.get().storeCountryList(app, countryList)
                }
            }

            override fun onFailure(error: String) {
                Log.d(tag, "CountryFetchCallback onFailure: $error")
                headline.value = error
            }
        })
    }
}