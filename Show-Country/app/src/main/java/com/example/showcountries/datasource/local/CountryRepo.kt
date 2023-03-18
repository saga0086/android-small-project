package com.example.showcountries.datasource.local

import android.content.Context
import com.example.showcountries.data.Constants
import com.example.showcountries.data.Country
import com.example.showcountries.util.CountryDataTransfer

/***
 * For simplicity, just use shared_preference file.
 * If there are more complex data, replace with data base.
 */
class CountryRepo private constructor(){
    companion object {
        private var instance: CountryRepo? = null
            get() {
                if (field == null) {
                    field = CountryRepo()
                }
                return field
            }
        fun get(): CountryRepo {
            return instance!!
        }
    }

    /**
     *  Get list of Country from shared_preference.
     *  @param context the Context
     *  @return the list of Country saved in file or null if nothing in the file.
     * */
    fun getCountryList(context: Context): List<Country>? {
        val sharedPreferences = context
            .getSharedPreferences(Constants.SHARED_PREF_COUNTRY, Context.MODE_PRIVATE)
        val data = sharedPreferences.getString(Constants.KEY_SHARED_PREF_COUNTRY, null)
        if (data != null) {
            return CountryDataTransfer.convertJsonStringToListOfCountry(data)
        } else {
            return null
        }
    }

    /**
     *  Store a list of Country in local storage.
     *  @param context the Context
     *  @param data the list of Country to be saved
     * */
    fun storeCountryList(context: Context, data: List<Country>) {
        val sharedPreferences = context
            .getSharedPreferences(Constants.SHARED_PREF_COUNTRY, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val dataString = CountryDataTransfer.convertListOfCountryToJsonString(data)
        editor.putString(Constants.KEY_SHARED_PREF_COUNTRY, dataString)
        editor.apply()
    }
}