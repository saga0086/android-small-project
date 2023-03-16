package com.example.showcountries

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.showcountries.data.Country
import com.example.showcountries.data.CountryResponseData
import com.example.showcountries.databinding.ActivityMainBinding
import com.example.showcountries.datasource.remote.CountryFetchCallback
import com.example.showcountries.datasource.remote.CountryFetchService
import com.example.showcountries.util.CountryDataTransfer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CountryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "onCreate()")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadCountryList()
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

    /**
     *  Start a coroutine to load a list of country from server
     *  and then update the UI.
     * */
    fun loadCountryList() {
        updateHeadline(getString(R.string.title_loading_country))
        lifecycleScope.launch {
            Log.d(tag, "start loading country list")

            withContext(Dispatchers.IO) {
                CountryFetchService.get()
                    .fetchCountryList(object : CountryFetchCallback<List<CountryResponseData>> {
                        override fun onSuccess(data: List<CountryResponseData>) {
                            Log.d(tag, "CountryFetchCallback onSuccess: ${data.size}")
                            updateHeadline(getString(R.string.tile_country_list))
                            adapter.getDataList()
                                .addAll(CountryDataTransfer.get().getListOfCountry(data))
                            adapter.notifyDataSetChanged()
                        }

                        override fun onFailure(error: String) {
                            Log.d(tag, "CountryFetchCallback onFailure: $error")
                            updateHeadline(error)
                        }
                    })
            }
        }
    }
}