package com.example.showwmtproduct.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.example.showwmtproduct.data.Product
import com.example.showwmtproduct.databinding.FragmentProductDetailBinding
import com.example.showwmtproduct.util.ProductUtil

class ProductDetailFragment: Fragment() {
    private val tag = "ProductDetailFragment"
    private lateinit var binding: FragmentProductDetailBinding
    private var product: Product? = null
    companion object {
        fun newInstance(product: Product): ProductDetailFragment {
            val fragment = ProductDetailFragment()
            fragment.arguments = ProductUtil.productToBundle(product)
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "onCreate()")

        arguments?.let {
            this.product = ProductUtil.bundleToProduct(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        Log.d(tag, "onCreateView()")

        binding = FragmentProductDetailBinding.inflate(inflater)
        binding.product = this.product
        binding.image.load(this.product?.url)

        return binding.root
    }
}