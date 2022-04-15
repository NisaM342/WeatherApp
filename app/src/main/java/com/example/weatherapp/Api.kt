package com.example.weatherapp

import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET( "weather")
    suspend fun getCurrentConditions(
        @Query( "zip") zip: String,
        @Query( "units") units: String = "Imperial",
        @Query( "appid") appId: String = "236584cc1ac48ff46f99007a069a18d6",
    ) : CurrentConditions

    @GET( "forecast/daily")
    suspend fun getForecast(
        @Query( "zip") zip: String,
        @Query( "units") units: String = "Imperial",
        @Query( "appid") appId: String = "236584cc1ac48ff46f99007a069a18d6",
        @Query( "cnt") cnt: Int = 16
    ) : Forecast

    @GET("weather")
    suspend fun getCurrentConditionsLatLon(
        @Query("lat") lat: Float,
        @Query("lon") lon: Float,
        @Query( "units") units: String = "Imperial",
        @Query("appid") appId: String = "236584cc1ac48ff46f99007a069a18d6",
    ): CurrentConditions

    @GET("forecast/daily")
    suspend fun getForecastLatLon(
        @Query("lat") lat: Float,
        @Query("lon") lon: Float,
        @Query( "units") units: String = "Imperial",
        @Query("appid") appId: String = "236584cc1ac48ff46f99007a069a18d6",
        @Query( "cnt") cnt: Int = 16
    ): Forecast

}
