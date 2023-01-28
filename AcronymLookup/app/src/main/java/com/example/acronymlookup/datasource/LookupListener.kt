/*
 * Copyright 2023
 * Author: Wei
 */

package com.example.acronymlookup.datasource

interface LookupListener<T> {
    fun onSuccess(data: T)
    fun onFailure(error: String)
}