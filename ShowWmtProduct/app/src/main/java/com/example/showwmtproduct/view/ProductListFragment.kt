package com.example.showwmtproduct.view

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
import coil.load
import com.example.showwmtproduct.MainActivity
import com.example.showwmtproduct.data.Product
import com.example.showwmtproduct.databinding.ItemProductBinding
import com.example.showwmtproduct.databinding.ProductListFragmentBinding
import com.example.showwmtproduct.viewmodel.ProductViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProductListFragment: Fragment() {
    private val tag = "ProductListFragment"
    private var viewModel: ProductViewModel? = null
    private lateinit var binding: ProductListFragmentBinding
    private lateinit var adapter: PtAdapter

    override fun onResume() {
        super.onResume()
        Log.d(tag, "onResume()")

        val aty = requireActivity()
        if (aty is MainActivity) {
            if (this.viewModel == null) {
                Log.d(tag, "get view model in onResume().")
                this.viewModel = aty.getViewModel()
                loadAndObserver()
            }
        } else {
            Log.e(tag, "host activity is not MainActivity!!")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        Log.d(tag, "onCreateView()")

        this.binding = ProductListFragmentBinding.inflate(inflater)
        setupRecyclerView()

        loadAndObserver()

        return this.binding.root
    }

    fun loadAndObserver() {
        Log.d(tag, "loadAndObserver()")
        viewModel?.loadProductList()
        observerData()
    }

    fun observerData() {
        viewModel?.products?.observe(viewLifecycleOwner, object : Observer<List<Product>?> {
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
        val layoutManager = LinearLayoutManager(requireContext())
        this.binding.productrecyclerview.layoutManager = layoutManager
        this.adapter = PtAdapter(listOf())
        this.binding.productrecyclerview.adapter = this.adapter
        this.binding.productrecyclerview.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.d(tag, "onScrolled.")
                if (!recyclerView.canScrollVertically(1)
                    && viewModel?.hasMoreProduct() == true && viewModel?.isLoading() != true
                ) {
                    loadMoreProduct(adapter.itemCount)
                }
            }
        })
    }

    fun setViewModel(vm: ProductViewModel) {
        this.viewModel = vm
    }

    /**
     *  Load more product from server.
     *  @param offset the offset of product list
     * */
    fun loadMoreProduct(offset: Int) {
        Log.i(tag, "loadMoreProduct called.")
        this.viewModel?.addProductListByOffset(offset)
    }

    inner class PtAdapter(private var data: List<Product>):
        RecyclerView.Adapter<PtAdapter.ProductViewHolder>() {

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
            holder.binding.image.setOnClickListener(ItemClickListener(position))
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
    }

    inner class ItemClickListener(val position: Int): View.OnClickListener {
        override fun onClick(v: View?) {
            val activity = requireActivity()
            if (activity is MainActivity) {
                activity.launchProductDetail(position)
            } else{
                Log.d(tag, "the host activity is not MainActivity!")
            }
        }
    }
}