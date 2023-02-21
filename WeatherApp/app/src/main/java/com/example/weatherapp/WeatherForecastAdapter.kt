package com.example.weatherapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.ForecastWeatherEntity
import com.example.weatherapp.databinding.ItemWeatherForecastBinding
import com.example.weatherapp.data.Constants.ICON_PREFIX

class WeatherForecastAdapter(private val data: MutableList<ForecastWeatherEntity>):
    RecyclerView.Adapter<WeatherForecastAdapter.ForecastWeatherViewHolder>(){
    override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): ForecastWeatherViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemWeatherForecastBinding.inflate(layoutInflater, viewGroup, false)
        return ForecastWeatherViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ForecastWeatherViewHolder, position: Int) {
        viewHolder.bind(this.data[position])
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    fun getDataList(): MutableList<ForecastWeatherEntity>{
        return this.data
    }

    inner class ForecastWeatherViewHolder(private val binding: ItemWeatherForecastBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ForecastWeatherEntity) {
            binding.weatherForecastEntity = item
            val idt = binding.root.context.resources
                .getIdentifier(ICON_PREFIX+item.icon, null, binding.root.context.packageName)
            binding.conditionIcon.setImageResource(idt)
            binding.executePendingBindings()
        }
    }
}