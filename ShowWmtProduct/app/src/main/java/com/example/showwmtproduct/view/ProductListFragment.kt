package com.example.showwmtproduct.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.showwmtproduct.data.Product
import com.example.showwmtproduct.databinding.ProductListFragmentBinding
import com.example.showwmtproduct.viewmodel.ProductViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProductListFragment(private val context: Context, private val viewModel: ProductViewModel): Fragment() {
    private val tag = "ProductListFragment"
    private lateinit var binding: ProductListFragmentBinding
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        this.binding = ProductListFragmentBinding.inflate(inflater)
        setupRecyclerView()

        viewModel.loadProductList()

        observerData()

        return this.binding.root
    }

    fun observerData() {
        viewModel.products.observe(viewLifecycleOwner, object : Observer<List<Product>?> {
            override fun onChanged(data: List<Product>?) {
                Log.d(tag, "Product list onChanged")
                lifecycleScope.launch(Dispatchers.Main) {
                    data?.let {
                        /*val diffResult = withContext(Dispatchers.IO) {
                            DiffUtil.calculateDiff(ProductDiffUtilCallback(adapter.getData(), it))
                        } */
                        val diffResult = DiffUtil
                            .calculateDiff(ProductDiffUtilCallback(adapter.getData(), it))
                        adapter.setData(it)
                        diffResult.dispatchUpdatesTo(adapter)
                    } ?: kotlin.run {
                        Log.d(tag, "Product list is null")
                    }
                }
            }
        })
    }

    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this.context)
        this.binding.productrecyclerview.layoutManager = layoutManager
        this.adapter = ProductAdapter(listOf(), this.context)
        this.binding.productrecyclerview.adapter = this.adapter
        this.binding.productrecyclerview.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.d(tag, "onScrolled.")
                if (!recyclerView.canScrollVertically(1)
                    && viewModel.hasMoreProduct() && !viewModel.isLoading()) {
                    loadMoreProduct(adapter.itemCount)
                }
            }
        })
    }

    /**
     *  Load more product from server.
     *  @param offset the offset of product list
     * */
    fun loadMoreProduct(offset: Int) {
        Log.i(tag, "loadMoreProduct called.")
        this.viewModel.addProductListByOffset(offset)
    }
}