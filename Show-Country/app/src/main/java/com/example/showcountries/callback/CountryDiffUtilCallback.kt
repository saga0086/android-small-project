package com.example.showcountries.callback

import androidx.recyclerview.widget.DiffUtil
import com.example.showcountries.data.Country

class CountryDiffUtilCallback(oldD: List<Country>, newD: List<Country>) : DiffUtil.Callback() {
    private val oldData = oldD
    private val newData = newD
    override fun getOldListSize(): Int {
        return oldData.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].code.equals(newData[newItemPosition].code)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].equals(newData[newItemPosition])
    }
}