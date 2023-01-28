/*
 * Copyright 2023
 * Author: Wei
 */

package com.example.acronymlookup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.acronymlookup.databinding.ItemFullformBinding
import com.example.acronymlookup.datamodel.Fullform

class FullformAdapter(private val data: MutableList<Fullform>) :
    RecyclerView.Adapter<FullformAdapter.FullformViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): FullformViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemFullformBinding.inflate(layoutInflater, viewGroup, false)
        return FullformViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: FullformViewHolder, position: Int) {
        viewHolder.bind(this.data[position])
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    fun getDataList(): MutableList<Fullform>{
        return this.data
    }

    inner class FullformViewHolder(private val binding: ItemFullformBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Fullform) {
            binding.fullform = item
            binding.executePendingBindings()
        }
    }
}