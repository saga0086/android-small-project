package com.example.acronymlookup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.acronymlookup.datasource.AcronymLookupService
import com.example.acronymlookup.datasource.remote.RemoteAcronymLookupService

class LookupViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LookupViewModel(RemoteAcronymLookupService()) as T
    }
}