package com.example.showwmtproduct.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.showwmtproduct.data.Product
import com.example.showwmtproduct.databinding.ItemProductBinding

class ProductAdapter(private var data: List<Product>, private val context: Context):
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(layoutInflater, parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = this.data[position]
        holder.bind(product)
        holder.binding.image.load(product.url)
        holder.binding.image.setOnClickListener(ItemClickListener(product.url, product.title,
            product.id, product.description))
    }

    fun getData(): List<Product> {
        return this.data
    }

    fun setData(list: List<Product>) {
        val newList = mutableListOf<Product>()
        newList.addAll(list)
        this.data = newList
    }

    inner class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            binding.product = item
            binding.executePendingBindings()
        }
    }

    inner class ItemClickListener(val url: String, val title: String, val id: Int,
                                  val description: String): OnClickListener{
        override fun onClick(v: View?) {
            TODO("Not yet implemented")
        }
    }
}