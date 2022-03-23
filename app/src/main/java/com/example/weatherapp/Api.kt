package com.example.weatherapp

import android.telecom.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("weather")
    suspend fun getCurrentConditions(
        @Query("zip") zip: String = "55119",
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = "236584cc1ac48ff46f99007a069a18d6",
    ): CurrentConditions
    @GET("forecast/daily")
    suspend fun getForecast(
        @Query("zip") zip: String = "55119",
        @Query("units") units: String = "imperial",
        @Query("appid") appId: String = "236584cc1ac48ff46f99007a069a18d6",
        @Query("cnt") cnt: Int = 16
    ): Forecast
}
