package com.example.showpointsgraph

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.showpointsgraph.databinding.ActivityMainBinding
import com.example.showpointsgraph.viewmodel.PointViewModel

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: PointViewModel
    lateinit var pointAdapter: PointAdapter
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(PointViewModel::class.java)
        setupSparkViewAndUpdate()
        viewModel.startUpdate()
    }

    fun setupSparkViewAndUpdate() {
        pointAdapter = PointAdapter(10)
        binding.sparkview.adapter = pointAdapter
        viewModel.liveData.observe(this, object : Observer<Float?> {
            override fun onChanged(num: Float?) {
                num?.let {
                    pointAdapter.addPoint(it)
                }
            }
        })
    }
}