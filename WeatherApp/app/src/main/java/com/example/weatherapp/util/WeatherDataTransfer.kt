package com.example.weatherapp.util

import android.util.Log
import com.example.weatherapp.data.*
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.util.Date

class WeatherDataTransfer {
    private val tag = "WeatherDataTransfer"
    companion object {
        private var instance: WeatherDataTransfer? = null
            get() {
                if (field == null) {
                    field = WeatherDataTransfer()
                }
                return field
            }
        fun get(): WeatherDataTransfer {
            return instance!!
        }
    }

    /**
     *  Convert CurrentWeatherResponse returned from server to CurrentWeather
     *  @param response the CurrentWeatherResponse
     * **/
    fun convertCurrentWeatherResponse(response: CurrentWeatherResponse): CurrentWeather {
        val icon = if (response.weather.isEmpty()){
            Log.d(tag, "response weather is empty")
            ""
        }else{
            response.weather[0].icon
        }
        val weatherName = if (response.weather.isEmpty())"" else response.weather[0].main
        return CurrentWeather(response.name, response.sys.country, Date(response.dt * 1000).toString(), weatherName,
            icon, response.main.temp, response.main.feels_like)
    }

    /**
     *  Convert ForecastWeatherResponse returned from server to ForecastWeather
     *  @param response the ForecastWeatherResponse
     * **/
    fun convertForecastWeatherResponse(response: ForecastWeatherResponse): ForecastWeather {
        return ForecastWeather(getListOfForecastWeatherEntity(response.list),
            response.city.name, response.city.country)
    }

    /**
     *  Convert a list of ForecastWeatherResponseEntity to a list of ForecastWeatherEntity
     *  @param list the list of ForecastWeatherResponseEntity
     * **/
    fun getListOfForecastWeatherEntity(list: List<ForecastWeatherResponseEntity>): List<ForecastWeatherEntity>{
        val result = mutableListOf<ForecastWeatherEntity>()
        for (entity in list) {
            val weatherCondition = if (entity.weather.size>0)entity.weather[0] else null
            val weatherName = weatherCondition?.main ?: ""
            val icon = weatherCondition?.icon ?: ""
            result.add(
                ForecastWeatherEntity(Date(entity.dt * 1000).toString(), weatherName,
                icon, entity.main.temp, entity.main.feels_like)
            )
        }
        return result
    }

    /**
     *  Convert ForecastWeather to Json string
     *  @param data the ForecastWeather object
     * **/
    fun convertForecastWeatherToJsonString(data: ForecastWeather): String {
        return Gson().toJson(data)
    }

    /**
     *  Convert Json string to ForecastWeather
     *  @param data the Json string
     * **/
    fun convertJsonStringToForecastWeather(data: String): ForecastWeather {
        return Gson().fromJson(data, ForecastWeather::class.java)
    }

    /**
     *  Convert CurrentWeather to Json string
     *  @param data the CurrentWeather object
     * **/
    fun convertCurrentWeatherToJsonString(data: CurrentWeather): String {
        return Gson().toJson(data)
    }

    /**
     *  Convert Json string to CurrentWeather
     *  @param data the Json string
     * **/
    fun convertJsonStringToCurrentWeather(data: String): CurrentWeather {
        return Gson().fromJson(data, CurrentWeather::class.java)
    }
}