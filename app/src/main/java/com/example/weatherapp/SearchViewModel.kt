package com.example.weatherapp

import android.util.Log
import androidx.appcompat.app.ActionBar
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
import kotlin.random.Random

class SearchViewModel :ViewModel() {
    private var zipCode: String? = null
    private val ourEnableButton = MutableLiveData<Boolean>(false)
    private val _showErrorDialog = MutableLiveData<Boolean>(false )

    val showErrorDialog: LiveData<Boolean>
        get() = _showErrorDialog

    val enableButton: LiveData<Boolean>
        get() = ourEnableButton

    fun updateZipCode(zipCode: String){
        if(zipCode != this.zipCode) {
            this.zipCode = zipCode
            ourEnableButton.value = isValidZipCode(zipCode)
        }
    }


    private fun isValidZipCode(zipCode: String): Boolean{
        return zipCode.length == 5 && zipCode.all { it.isDigit() }
    }

    fun getZipCode(): String{
        return zipCode!!
    }

    fun searchButtonClicked() {
        Log.d(SearchViewModel::class.java.simpleName, zipCode ?: "No Zip yet!")
        _showErrorDialog.value = Random.nextBoolean()
    }

}