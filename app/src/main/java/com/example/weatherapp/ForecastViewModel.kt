package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class ForecastViewModel @Inject constructor(private val service: Api): ViewModel() {
     val mutateForecast = MutableLiveData<Forecast>()


    fun loadData(myZip: String) = runBlocking {
        launch {
            mutateForecast.value = service.getForecast(myZip)
        }
    }
}
