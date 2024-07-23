package com.example.showwmtproduct.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import com.example.showwmtproduct.MainActivity
import com.example.showwmtproduct.data.Product
import com.example.showwmtproduct.databinding.FragmentProductDetailBinding
import com.example.showwmtproduct.viewmodel.ProductViewModel

class ProductDetailFragment: Fragment() {
    private val tag = "ProductDetailFragment"
    private val KEY_CUR_POS = "curPos"
    private lateinit var binding: FragmentProductDetailBinding
    private lateinit var viewPagerAdapter: ProductDetailPagerAdapter
    private var viewModel: ProductViewModel? = null
    private var curPosition = 0
    companion object {
        fun newInstance(start: Int, vm: ProductViewModel): ProductDetailFragment {
            val fragment = ProductDetailFragment()
            fragment.curPosition = start
            fragment.viewModel = vm
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(tag, "onCreate()")

        savedInstanceState?.let {
            val start = it.getInt(KEY_CUR_POS, -1)
            Log.d(tag, "Restore current position: $start")
            curPosition = if (start >= 0)start else 0
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        Log.i(tag, "onCreateView()")

        binding = FragmentProductDetailBinding.inflate(inflater)
        viewPagerAdapter = ProductDetailPagerAdapter(listOf())
        binding.productDetailPager.adapter = viewPagerAdapter

        viewModel?.let {
            setDataToAdapter(it.getData())
            binding.productDetailPager
                .setCurrentItem(curPosition, false)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Log.i(tag, "onStart()")

        val aty = requireActivity()
        if (aty is MainActivity) {
            if (viewModel == null) {
                viewModel = aty.getViewModel()
                Log.d(tag, "Get ViewModel in onStart()")
                viewModel?.let {
                    setDataToAdapter(it.getData())
                    binding.productDetailPager
                        .setCurrentItem(curPosition, false)
                } ?: kotlin.run {
                    Log.e(tag, "view model is still null in activity!!!")
                }
            }
        } else {
            Log.e(tag, "host activity is not MainActivity!!")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(tag, "onSaveInstanceState()")

        outState.putInt(KEY_CUR_POS, binding.productDetailPager.currentItem)
    }

    private fun setDataToAdapter(data: List<Product>) {
        Log.d(tag, "setDataToAdapter()")

        val diffResult = DiffUtil.calculateDiff(ProductDiffUtilCallback(
            viewPagerAdapter.getData(), data))
        viewPagerAdapter.setData(data)
        diffResult.dispatchUpdatesTo(viewPagerAdapter)
    }

}