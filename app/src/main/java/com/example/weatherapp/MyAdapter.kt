package com.example.weatherapp

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MyAdapter (private val data: List<DayForecast>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    class ViewHolder (val view: View) : RecyclerView.ViewHolder(view) {
        private val dateView: TextView = view.findViewById(R.id.WeatherDate)
        private val sunriseView: TextView = view.findViewById(R.id.Sunrise)
        private val sunsetView: TextView = view.findViewById(R.id.Sunset)
        private val tempViewHigh: TextView = view.findViewById(R.id.WeatherHigh)
        private val tempViewLow: TextView = view.findViewById(R.id.WeatherLow)
        private val tempView: TextView = view.findViewById(R.id.WeatherTemp)
        private val theImage: ImageView = view.findViewById(R.id.weatherImage)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(data: DayForecast) {
            dateView.text = data.date.toString()
            theImage.setImageDrawable(view.context.getDrawable(data.image))


            val instant = Instant.ofEpochSecond(data.date)
            val dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            val formatter = DateTimeFormatter.ofPattern("MMM dd")
            dateView.text = formatter.format(dateTime)


            tempView.text = "Temp: " + data.temp.day.toInt().toString() + "°"

            tempViewHigh.text = "High: " + data.temp.high.toInt().toString() + "°"

            tempViewLow.text = "Low: " +  data.temp.low.toInt().toString() + "°"


            val sunriseInstant = Instant.ofEpochSecond(data.sunrise)
            val sunriseTime = LocalDateTime.ofInstant(sunriseInstant, ZoneId.systemDefault())
            val sunriseFormatter = DateTimeFormatter.ofPattern("HH:MM a")

            sunriseView.text = "Sunrise: "+ sunriseFormatter.format(sunriseTime)

            val sunsetInstant = Instant.ofEpochSecond(data.sunset)
            val sunsetTime = LocalDateTime.ofInstant(sunsetInstant, ZoneId.systemDefault())
            val sunsetFormatter = DateTimeFormatter.ofPattern("HH:MM a")

            sunsetView.text = "Sunset: "+ sunsetFormatter.format(sunsetTime)
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
