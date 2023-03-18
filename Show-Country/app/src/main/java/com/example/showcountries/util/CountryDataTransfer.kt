package com.example.showcountries.util

import com.example.showcountries.data.Country
import com.example.showcountries.data.CountryResponseData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object CountryDataTransfer {

    /**
     *  Convert a list of CountryResponseData returned from server to a list of Country
     *  @param response the list of CountryResponse
     *  @return the list of Country
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
     *  @return the Country object
     * **/
    fun convertCountryResponse(data: CountryResponseData): Country {
        return Country(data.name, data.region, data.code, data.capital)
    }

    /**
     *  Convert a list of Country to Json string
     *  @param data the list of Country
     *  @return the Json string
     * **/
    fun convertListOfCountryToJsonString(data: List<Country>): String {
        return Gson().toJson(data)
    }

    /**
     *  Convert Json string to a list of Country object
     *  @param data the Json string
     *  @return the list of Country
     * **/
    fun convertJsonStringToListOfCountry(data: String): List<Country> {
        val type = object : TypeToken<List<Country>>() {}.type
        return Gson().fromJson<List<Country>>(data, type)
    }
}