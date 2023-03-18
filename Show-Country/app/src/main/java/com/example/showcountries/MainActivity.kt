package com.example.showcountries

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.showcountries.data.Country
import com.example.showcountries.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CountryAdapter
    private lateinit var viewModel: CountryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "onCreate()")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        viewModel = ViewModelProvider(this).get(CountryViewModel::class.java)
        observerData()
    }

    fun updateHeadline(message: String) {
        binding.headline.visibility = View.VISIBLE
        binding.headline.setText(message)
    }

    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        this.binding.recyclerview.layoutManager = layoutManager
        this.adapter = CountryAdapter(mutableListOf<Country>())
        this.binding.recyclerview.adapter = this.adapter
    }

    fun observerData() {
        viewModel.countries.observe(this, object : Observer<List<Country>?> {
            override fun onChanged(data: List<Country>?) {
                Log.d(tag, "country list onChanged")
                adapter.getDataList().clear()
                data?.let {
                    adapter.getDataList().addAll(it)
                } ?: kotlin.run {
                    Log.d(tag, "country list is null")
                }
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.headline.observe(this, object : Observer<String?> {
            override fun onChanged(data: String?) {
                Log.d(tag, "head line onChanged")
                data?.let {
                    updateHeadline(it)
                } ?: kotlin.run {
                    Log.d(tag, "head line is null")
                }
            }
        })
    }
}