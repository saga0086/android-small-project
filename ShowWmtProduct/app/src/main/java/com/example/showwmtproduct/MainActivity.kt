package com.example.showwmtproduct

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.showwmtproduct.databinding.ActivityMainBinding
import com.example.showwmtproduct.view.ProductDetailPagerAdapter
import com.example.showwmtproduct.view.ProductListFragment
import com.example.showwmtproduct.viewmodel.ProductViewModel

class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    private val fragmentTag = "ptListFragment"
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ProductViewModel
    //private var state = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(tag, "onCreate()")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        val productListFragment = ProductListFragment()
        productListFragment.setViewModel(this.viewModel)

        binding.button.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_main, productListFragment, fragmentTag)
                .commit()
            it.visibility = View.GONE
        }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Log.d(tag, "on back pressed.")
                if (binding.productDetailPager.visibility == View.VISIBLE) {
                    Log.d(tag, "returning to product list page.")
                    binding.productDetailPager.adapter = null
                    binding.productDetailPager.visibility = View.GONE

                    val fragment = supportFragmentManager.findFragmentByTag(fragmentTag)
                    fragment?.let {
                        supportFragmentManager.beginTransaction()
                            .show(it)
                            .commit()
                    } ?: kotlin.run {
                        Log.e(tag, "Can't find fragment by tag!!")
                    }
                } else {
                    finish()
                }
            }
        })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(tag, "onConfigurationChanged()")
    }

    fun getViewModel(): ProductViewModel {
        return this.viewModel
    }


    /**
    * launch product detail page of a specific item.
     * @param position the position of the item in the list.
    * */
    fun launchProductDetail(position: Int) {
        Log.i(tag, "launching product detail.")

        binding.productDetailPager.adapter =
            ProductDetailPagerAdapter(this, viewModel.getData())

        val productListFragment = supportFragmentManager.findFragmentByTag(fragmentTag)
        productListFragment?.let {
            supportFragmentManager.beginTransaction()
                .hide(it)
                .commit()
        } ?: kotlin.run {
            Log.e(tag, "Can't find fragment by tag. $fragmentTag")
        }

        binding.productDetailPager.visibility = View.VISIBLE
        binding.productDetailPager.setCurrentItem(position, false)
    }
}