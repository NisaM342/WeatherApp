package com.example.weatherapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.DataRowBinding
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MyAdapter(private val data: List<DayForecast>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    @SuppressLint("NewApi")
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = DataRowBinding.bind(view)
        private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")
        private val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")

        fun bind(data: DayForecast) {
            val dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(data.dt), ZoneId.systemDefault())
            val sunrise = LocalDateTime.ofInstant(Instant.ofEpochSecond(data.sunrise), ZoneId.systemDefault())
            val sunset = LocalDateTime.ofInstant(Instant.ofEpochSecond(data.sunset), ZoneId.systemDefault())
            val weather = data.weather.firstOrNull()?.icon
            Glide.with(binding.forecastIcon)
                .load("https://openweathermap.org/img/wn/${weather}@2x.png")
                .into(binding.forecastIcon)

            binding.date.text = dateFormatter.format(dateTime)
            binding.sunrise.append(timeFormatter.format(sunrise))
            binding.sunset.append(timeFormatter.format(sunset))
            binding.temp.append(data.temp.day.toInt().toString() + "°")
            binding.high.append(data.temp.max.toInt().toString() + "°")
            binding.low.append(data.temp.min.toInt().toString() + "°")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.data_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

}