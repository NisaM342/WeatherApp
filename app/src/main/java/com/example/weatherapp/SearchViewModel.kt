package com.example.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import kotlin.random.Random

class SearchViewModel @Inject constructor() : ViewModel() {

    private var zipCode: String? = null
    private val _enableButton = MutableLiveData<Boolean>(false)

    val enableButton: LiveData<Boolean>
        get() = _enableButton

    private val _showErrorDialog = MutableLiveData<Boolean>(false)
    val showErrorDialog: LiveData<Boolean>
        get() = _showErrorDialog

    fun updateZipCode(zipCode: String) {
        if (zipCode != this.zipCode) {
            this.zipCode = zipCode
            _enableButton.value = isValidZipCode(zipCode)
        }
    }

    private fun isValidZipCode(zipCode: String): Boolean {
        return zipCode.length == 5 && zipCode.all { it.isDigit() }

    }

    fun submitButtonClicked() {
        Log.d(SearchViewModel::class.java.simpleName, zipCode ?: "No Zip yet!")
        _showErrorDialog.value = Random.nextBoolean()
    }

    fun getZip() : String
    {
        return zipCode!!
    }

}