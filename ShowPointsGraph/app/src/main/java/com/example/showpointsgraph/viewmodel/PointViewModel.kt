package com.example.showpointsgraph.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.showpointsgraph.datasource.remote.NumberCallback
import com.example.showpointsgraph.datasource.remote.NumberFetchService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PointViewModel(val app: Application): AndroidViewModel(app) {
    val tag = "PointViewModel"
    val liveData = MutableLiveData<Float?>()
    private var job: Job? = null

    fun startUpdate() {
        Log.d(tag, "startUpdate called")
        if (job == null) {
            job = viewModelScope.launch {
                Log.d(tag, "startUpdate launching")
                val service = NumberFetchService()
                val callback = object : NumberCallback {
                    override fun onSuccess(number: String?) {
                        number?.let {
                            Log.d(tag, "received number: $number")
                            liveData.value = it.toFloatOrNull()
                        } ?: kotlin.run {
                            Log.d(tag, "number is null")
                        }
                    }
                    override fun onFail(t: Throwable?) {
                        Log.d(tag, "onFail: ${t?.message}")
                    }
                }
                while (true) {
                    service.fetchNumberPlain(callback)
                    delay(1000)
                }
            }
        }
    }

    fun stopUpdate() {
        job?.cancel()
        job = null
    }
}