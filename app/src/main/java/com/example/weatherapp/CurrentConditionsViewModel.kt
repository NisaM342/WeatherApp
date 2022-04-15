package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class CurrentConditionsViewModel @Inject constructor(private val service: Api): ViewModel() {

    private val theCurrentConditions = MutableLiveData<CurrentConditions>()
    val currentConditions: LiveData<CurrentConditions>
        get() = theCurrentConditions

    fun loadData(zipCode: String) = runBlocking {
        launch {
            theCurrentConditions.value = service.getCurrentConditions(zipCode)
        }
    }

    fun loadData(latitude: Float, longitude: Float) = runBlocking {
        launch{
            theCurrentConditions.value = service.getCurrentConditionsLatLon(latitude, longitude)
        }
    }
}
