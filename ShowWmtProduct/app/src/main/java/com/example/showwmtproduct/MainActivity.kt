package com.example.showwmtproduct

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.showwmtproduct.databinding.ActivityMainBinding
import com.example.showwmtproduct.view.ProductListFragment
import com.example.showwmtproduct.viewmodel.ProductViewModel

class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ProductViewModel
    private lateinit var productListFragment: ProductListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "onCreate()")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        productListFragment = ProductListFragment(this, viewModel)

        binding.productDetailPager.visibility = View.GONE

        binding.button.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_main, productListFragment)
                .commit()
            it.visibility = View.GONE
        }
        binding.button.visibility = View.VISIBLE
    }
}