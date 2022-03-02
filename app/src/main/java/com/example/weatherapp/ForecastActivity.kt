package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ForecastActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private val adapterData = listOf<DayForecast>(



        DayForecast( R.drawable.sun, date= 1643656200, sunrise = 1644994800, sunset = 1645034400,
            temp = ForecastTemp(42F, 42F, 55F)),
        DayForecast( R.drawable.sun, date= 1643742600,  sunrise = 1644994800, sunset = 1645034400,
            temp = ForecastTemp(23F, 23F, 32F)),
        DayForecast( R.drawable.sun, date= 1643829000,  sunrise = 1644994800, sunset = 1645034400,
            temp = ForecastTemp(36F, 34F, 44F)),
        DayForecast( R.drawable.sun, date= 1643915400,  sunrise = 1644994800, sunset = 1645034400,
            temp = ForecastTemp(57F, 41F, 60F)),
        DayForecast( R.drawable.sun, date= 1644001800,  sunrise = 1644994800, sunset = 1645034400,
            temp = ForecastTemp(58F, 40F, 61F)),
        DayForecast( R.drawable.sun, date= 1644088200,  sunrise = 1644994800, sunset = 1645034400,
            temp = ForecastTemp(19F, 11F, 23F)),
        DayForecast( R.drawable.sun, date= 1644174600,  sunrise = 1644994800, sunset = 1645034400,
            temp = ForecastTemp(2F, -1F, 8F)),
        DayForecast( R.drawable.sun, date= 1644261000,  sunrise = 1644994800, sunset = 1645034400,
            temp = ForecastTemp(11F, 2F, 11F)),
        DayForecast( R.drawable.sun, date= 1644347400,  sunrise = 1644994800, sunset = 1645034400,
            temp = ForecastTemp(42F, 40F, 50F)),
        DayForecast( R.drawable.sun, date= 1644520200,  sunrise = 1644994800, sunset = 1645034400,
            temp = ForecastTemp(43F, 48F, 58F)),
        DayForecast( R.drawable.sun, date= 1644606600,  sunrise = 1644994800, sunset = 1645034400,
            temp = ForecastTemp(64F, 60F, 67F)),
        DayForecast( R.drawable.sun, date= 1644779400,  sunrise = 1644994800, sunset = 1645034400,
            temp = ForecastTemp(45F, 40F, 49F)),
        DayForecast( R.drawable.sun, date= 1644865800,  sunrise = 1644994800, sunset = 1645034400,
            temp = ForecastTemp(46F, 40F, 52F)),
        DayForecast( R.drawable.sun, date= 1644952200,  sunrise = 1644994800, sunset = 1645034400,
            temp = ForecastTemp(67F, 64F, 69F)),




        )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        recyclerView = findViewById(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = MyAdapter(adapterData)


    }
}