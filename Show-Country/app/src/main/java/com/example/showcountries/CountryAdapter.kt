package com.example.showcountries

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.showcountries.data.Country
import com.example.showcountries.databinding.ItemCountryBinding

class CountryAdapter(private var data: List<Country>):
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>(){
    override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): CountryViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemCountryBinding.inflate(layoutInflater, viewGroup, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: CountryViewHolder, position: Int) {
        viewHolder.bind(this.data[position])
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    fun getDataList(): List<Country>{
        return this.data
    }

    fun setDataList(newData: List<Country>) {
        this.data = newData
    }

    inner class CountryViewHolder(private val binding: ItemCountryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Country) {
            binding.country = item
            binding.executePendingBindings()
        }
    }
}