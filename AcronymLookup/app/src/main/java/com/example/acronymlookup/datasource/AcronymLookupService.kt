/*
 * Copyright 2023
 * Author: Wei
 */

package com.example.acronymlookup.datasource

import com.example.acronymlookup.datamodel.LookupData

interface AcronymLookupService {
    fun lookup(acronym: String, listener: LookupListener<List<LookupData>>)
}