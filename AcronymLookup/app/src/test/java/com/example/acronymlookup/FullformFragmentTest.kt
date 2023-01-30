/*
 * Copyright 2023
 * Author: Wei
 */

package com.example.acronymlookup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.Mockito.*

import com.example.acronymlookup.datasource.AcronymLookupService

class FullformFragmentTest {
    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun testFetchData() {
        val lookupService = mock(AcronymLookupService::class.java)
        val lookupViewModel = FullformFragment().LookupViewModel(lookupService)
        val name = "testName"

        lookupViewModel.fetchData(name)
        verify(lookupService).lookup(name, lookupViewModel.lookupListener)
        assertTrue(lookupViewModel.isLoading.value!!)
        assertTrue(lookupViewModel.errorMessage.value!!.isEmpty())
    }
}