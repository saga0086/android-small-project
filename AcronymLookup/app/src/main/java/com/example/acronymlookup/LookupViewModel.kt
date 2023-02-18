package com.example.acronymlookup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.acronymlookup.datamodel.Fullform
import com.example.acronymlookup.datamodel.LookupData
import com.example.acronymlookup.datasource.AcronymLookupService
import com.example.acronymlookup.datasource.LookupListener
class LookupViewModel(service: AcronymLookupService): ViewModel() {
    private val lookupService = service
    val fullFormsData: MutableLiveData<List<Fullform>> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    val lookupListener = object: LookupListener<List<LookupData>> {
        override fun onSuccess(data: List<LookupData>) {
            if (data.isEmpty() || data[0].lfs.isEmpty()) {
                errorMessage.value = "Full form is not found..."
            } else{
                fullFormsData.value = data[0].lfs
            }
        }
        override fun onFailure(error: String) {
            errorMessage.value = error
        }
    }

    fun fetchData(name: String) {
        errorMessage.value = ""

        lookupService.lookup(name, lookupListener)
    }
}