package com.example.simpleweatherapp.business

import com.example.simpleweatherapp.business.model.WeatherDataModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/onecall?")
    fun getWeatherForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String = "minutely,alerts",
        @Query("appid") apiId: String = "f09aa410fc1c63bebcf10676a0bc8004",
        @Query("lang") lang: String = "en"
    ) : Observable<WeatherDataModel>
}