package com.example.showwmtproduct.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.showwmtproduct.MainActivity
import com.example.showwmtproduct.R
import com.example.showwmtproduct.data.Product
import com.example.showwmtproduct.databinding.ItemProductBinding
import com.example.showwmtproduct.databinding.ProductListFragmentBinding
import com.example.showwmtproduct.viewmodel.ProductViewModel


class ProductListFragment: Fragment() {
    private val tag = "ProductListFragment"
    private var viewModel: ProductViewModel? = null
    private lateinit var binding: ProductListFragmentBinding
    private lateinit var adapter: PtAdapter
    companion object {
        fun newInstance(vm: ProductViewModel): ProductListFragment {
            val fragment = ProductListFragment()
            fragment.viewModel = vm
            return fragment
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(tag, "onStart()")

        val aty = requireActivity()
        if (aty is MainActivity) {
            if (this.viewModel == null) {
                Log.d(tag, "get view model in onStart().")
                this.viewModel = aty.getViewModel()
                this.viewModel?.let {
                    if (it.getDataSize() > 0) {
                        Log.d(tag, "Have existing products, size: ${it.getDataSize()}")
                        setDataToAdapter(it.getData())
                        observerData()
                    } else {
                        Log.d(tag, "Product list is empty, load it from server.")
                        loadAndObserve()
                    }
                } ?: kotlin.run {
                    Log.e(tag, "view model is still null in activity!!!")
                }
            }
        } else {
            Log.e(tag, "host activity is not MainActivity!!")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        Log.i(tag, "onCreateView()")

        this.binding = ProductListFragmentBinding.inflate(inflater)
        setupRecyclerView()

        viewModel?.let {
            if (it.getDataSize() == 0) {
                Log.d(tag, "onCreateView product list is empty, load it from server.")
                loadAndObserve()
            } else {
                Log.d(tag, "onCreateView have existing products, size: ${it.getDataSize()}")
                setDataToAdapter(it.getData())
                observerData()
            }
        }

        return this.binding.root
    }

    /**
     *  Load products data from server and observe the live data.
     * */
    private fun loadAndObserve() {
        Log.i(tag, "loadAndObserver()")
        showProgressBig()
        viewModel?.loadProductList()
        observerData()
    }

    /**
     *  Set list of products to adapter.
     *  @param data the list of products
     * */
    private fun setDataToAdapter(data: List<Product>) {
        Log.d(tag, "setDataToAdapter(), size: ${data.size}")
        val diffResult = DiffUtil
            .calculateDiff(ProductDiffUtilCallback(adapter.data, data))
        adapter.copyAndSetData(data)
        diffResult.dispatchUpdatesTo(adapter)
    }

    private fun observerData() {
        viewModel?.products?.observe(viewLifecycleOwner
        ) { value ->
            Log.d(tag, "Product list onChanged")
            value?.let {
                /*val diffResult = withContext(Dispatchers.IO) {
                           DiffUtil
                           .calculateDiff(ProductDiffUtilCallback(
                           adapter.getData(), it))
                      } */
                setDataToAdapter(it)
            } ?: kotlin.run {
                Log.d(tag, "Product list is null")
            }
            hideProgress()
        }
    }

    private fun setupRecyclerView() {
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
                    showProgressSmall()
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
        this.viewModel?.addProductListByOffset(offset)
    }

    /**
     *  Show progress bar animation(big bar)
     * */
    private fun showProgressBig() {
        Log.d(tag, "showProgressBig called.")
        binding.progressBar.visibility = View.VISIBLE
        binding.progressBar.setImageResource(R.drawable.progress_bar1_big)
        val anm = AnimationUtils
            .loadAnimation(context, R.anim.progress_bar)
        binding.progressBar.startAnimation(anm)
    }

    /**
     *  Show progress bar animation(small bar)
     * */
    fun showProgressSmall() {
        Log.d(tag, "showProgressSmall called.")
        binding.progressBar.visibility = View.VISIBLE
        binding.progressBar.setImageResource(R.drawable.progress_bar1_small)
        val anm = AnimationUtils
            .loadAnimation(context, R.anim.progress_bar)
        binding.progressBar.startAnimation(anm)
    }

    /**
     *  Hide the progress bar.
     * */
    private fun hideProgress() {
        Log.d(tag, "hideProgress called.")
        binding.progressBar.clearAnimation()
        binding.progressBar.visibility = View.GONE
    }

    inner class PtAdapter(var data: List<Product>):
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

        fun copyAndSetData(list: List<Product>) {
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

    inner class ItemClickListener(private val position: Int): View.OnClickListener {
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