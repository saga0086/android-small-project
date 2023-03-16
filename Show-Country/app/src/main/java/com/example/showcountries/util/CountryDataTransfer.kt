package com.example.showcountries.util

import com.example.showcountries.data.Country
import com.example.showcountries.data.CountryResponseData

class CountryDataTransfer {
    companion object {
        private var instance: CountryDataTransfer? = null
            get() {
                if (field == null) {
                    field = CountryDataTransfer()
                }
                return field
            }
        fun get(): CountryDataTransfer {
            return instance!!
        }
    }

    /**
     *  Convert a list of CountryResponseData returned from server to a list of Country
     *  @param response the list of CountryResponse
     * **/
    fun getListOfCountry(response: List<CountryResponseData>): List<Country> {
        val result = mutableListOf<Country>()
        for (data in response) {
            result.add(Country(data.name, data.region, data.code, data.capital))
        }
        return result
    }

    /**
     *  Convert CountryResponseData to Country class
     *  @param data the CountryResponseData
     * **/
    fun convertCountryResponse(data: CountryResponseData): Country {
        return Country(data.name, data.region, data.code, data.capital)
    }
}