package com.example.showwmtproduct

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.showwmtproduct.databinding.ActivityMainBinding
import com.example.showwmtproduct.view.ProductDetailFragment
import com.example.showwmtproduct.view.ProductListFragment
import com.example.showwmtproduct.viewmodel.ProductViewModel

class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    private val KEY_STATE = "state"
    private val STATE_START = 0
    private val STATE_DISPLAY_LIST = 1
    private val STATE_DISPLAY_DETAIL = 2
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ProductViewModel
    private var state = STATE_START

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(tag, "onCreate()")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        binding.button.setOnClickListener {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container_main,
                        ProductListFragment.newInstance(viewModel))
                    .commit()
            state = STATE_DISPLAY_LIST
            it.visibility = View.GONE
        }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Log.d(tag, "on back pressed.")
                if (state == STATE_DISPLAY_DETAIL) {
                    Log.d(tag, "returning to product list page.")
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container_main,
                            ProductListFragment.newInstance(viewModel))
                        .commit()
                    state = STATE_DISPLAY_LIST
                } else {
                    finish()
                }
            }
        })

        savedInstanceState?.let {
            val sta = it.getInt(KEY_STATE, -1)
            Log.d(tag, "Restore state: $sta")
            state = if (sta >= 0)sta else STATE_START
        }

        if (state != STATE_START) {
            binding.button.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(tag, "onSaveInstanceState()")

        outState.putInt(KEY_STATE, state)
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

        supportFragmentManager.beginTransaction()
            .replace(R.id.container_main,
                ProductDetailFragment.newInstance(position, viewModel))
            .commit()
        state = STATE_DISPLAY_DETAIL
    }
}