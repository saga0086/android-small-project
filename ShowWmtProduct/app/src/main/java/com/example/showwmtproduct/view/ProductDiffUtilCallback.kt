package com.example.showwmtproduct.view

import androidx.recyclerview.widget.DiffUtil
import com.example.showwmtproduct.data.Product

class ProductDiffUtilCallback(val oldDate: List<Product>, val newData: List<Product>): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldDate.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDate[oldItemPosition].id == newData[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDate[oldItemPosition].url.equals(newData[newItemPosition].url) &&
                oldDate[oldItemPosition].title.equals(newData[newItemPosition].title)
    }
}