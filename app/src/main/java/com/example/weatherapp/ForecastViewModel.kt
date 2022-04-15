package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ForecastViewModel @Inject constructor(private val service: Api): ViewModel() {
    private val theForecast = MutableLiveData<Forecast>()
    val forecast: LiveData<Forecast>
        get() = theForecast

    fun loadData(zipCode: String) = runBlocking {
        launch {
            theForecast.value = service.getForecast(zipCode)
        }

    }

    fun loadData(latitude: Float, longitude: Float) = runBlocking {
        launch{
            theForecast.value = service.getForecastLatLon(latitude, longitude)
        }
    }

}
