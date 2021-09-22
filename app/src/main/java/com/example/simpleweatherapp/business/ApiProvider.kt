package com.example.simpleweatherapp.business

import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiProvider {

    private val openWeatherMaps: Retrofit by lazy { initApi() }

    private fun initApi() = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org/")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun provideWeatherApi(): WeatherApi = openWeatherMaps.create(WeatherApi::class.java)

    fun providerGeoCodeApi(): GeoCodingApi = openWeatherMaps.create(GeoCodingApi::class.java)

}