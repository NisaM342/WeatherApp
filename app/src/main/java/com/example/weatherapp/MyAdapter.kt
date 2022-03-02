package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.DayForecastBinding
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MyAdapter (private val data: List<DayForecast>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    @SuppressLint("NewApi")
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        private val binding = DayForecastBinding.bind(view)
        private val dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd")
        private val timeFormatter = DateTimeFormatter.ofPattern("h:mma")

        fun bind(data: DayForecast) {
            val dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(data.dt), ZoneId.systemDefault())
            val sunrise = LocalDateTime.ofInstant(Instant.ofEpochSecond(data.sunrise), ZoneId.systemDefault())
            val sunset = LocalDateTime.ofInstant(Instant.ofEpochSecond(data.sunset), ZoneId.systemDefault())
            binding.date.text = dateTimeFormatter.format(dateTime)
            binding.sunriseTime.text = timeFormatter.format(sunrise)
            binding.sunsetTime.text = timeFormatter.format(sunset)
            binding.dayTemp.text = data.temp.day.toInt().toString() + "°"
            binding.maxTemp.text = data.temp.max.toInt().toString() + "°"
            binding.minTemp.text = data.temp.min.toInt().toString() + "°"

            val weather = data.weather.firstOrNull()?.icon
            Glide.with(binding.forecastIcon)
                .load("https://openweathermap.org/img/wn/${weather}@2x.png")
                .into(binding.forecastIcon)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.data_row, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size
}
