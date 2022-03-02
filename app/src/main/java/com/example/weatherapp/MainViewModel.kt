package com.example.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MainViewModel @Inject constructor(private val service: Api): ViewModel() {
    private val mutateCurrentConditions = MutableLiveData<CurrentConditions>()
    val currentConditions: LiveData<CurrentConditions>
        get() = mutateCurrentConditions

    fun loadData() = runBlocking {
        launch {
            mutateCurrentConditions.value = service.getCurrentConditions()
        }
    }
}