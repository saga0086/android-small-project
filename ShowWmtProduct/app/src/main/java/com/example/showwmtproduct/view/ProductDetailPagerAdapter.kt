package com.example.showwmtproduct.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.showwmtproduct.data.Product
import com.example.showwmtproduct.databinding.ItemDetailProductBinding

class ProductDetailPagerAdapter(private var products: List<Product>)
    : RecyclerView.Adapter<ProductDetailPagerAdapter.PtDtViewHolder>() {
    private val tag = "ProductDetailPagerAdapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PtDtViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemDetailProductBinding.inflate(layoutInflater, parent, false)
        return PtDtViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PtDtViewHolder, position: Int) {
        Log.d(tag, "onBindViewHolder: $position")
        holder.bind(products[position])
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun setData(data: List<Product>) {
        this.products = data
    }
    
    fun getData(): List<Product> {
        return products
    }

    inner class PtDtViewHolder(val binding: ItemDetailProductBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            binding.product = item
            binding.image.load(item.url)
            binding.executePendingBindings()
        }
    }
}