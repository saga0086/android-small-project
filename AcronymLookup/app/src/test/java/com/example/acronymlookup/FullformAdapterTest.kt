/*
 * Copyright 2023
 * Author: Wei
 */

package com.example.acronymlookup

import org.junit.Test
import org.junit.Assert.assertTrue
import org.mockito.Mockito.*

import com.example.acronymlookup.datamodel.Fullform

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class FullformAdapterTest {

    @Test
    fun testGetDataList() {
        val data = mutableListOf<Fullform>()
        val adapter = FullformAdapter(data)
        assertTrue(data === adapter.getDataList())

        val fullName = "testLf"
        val freq = 11
        val since = 1990
        data.add(Fullform(fullName, freq, since))

        assertEquals(fullName, adapter.getDataList()[0].lf)
        assertEquals(freq, adapter.getDataList()[0].freq)
        assertEquals(since, adapter.getDataList()[0].since)
    }

    @Test
    fun testGetItemCount() {
        val data = mutableListOf<Fullform>()
        val adapter = FullformAdapter(data)
        assertEquals(0, adapter.itemCount)

        val fullName = "lfTest"
        val freq = 20
        val since = 1995
        data.add(Fullform(fullName, freq, since))
        assertEquals(1, adapter.itemCount)
    }

    @Test
    fun testOnBindViewHolder() {
        val mockViewHolder = mock(FullformAdapter.FullformViewHolder::class.java)
        val data = mutableListOf<Fullform>()
        data.add(Fullform("fullName", 1, 1))
        val adapter = FullformAdapter(data)

        adapter.onBindViewHolder(mockViewHolder, 0)
        verify(mockViewHolder).bind(data[0])
    }
}