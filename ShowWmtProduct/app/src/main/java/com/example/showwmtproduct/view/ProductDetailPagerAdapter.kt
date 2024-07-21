package com.example.showwmtproduct.view

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.showwmtproduct.data.Product

class ProductDetailPagerAdapter(aty: FragmentActivity, val products: List<Product>)
    : FragmentStateAdapter(aty) {
    private val tag = "ProductViewModel"

    override fun getItemCount(): Int {
        return products.size
    }

    override fun createFragment(position: Int): Fragment {
        Log.d(tag, "createFragment: $position")
        return ProductDetailFragment.newInstance(products[position])
    }
}