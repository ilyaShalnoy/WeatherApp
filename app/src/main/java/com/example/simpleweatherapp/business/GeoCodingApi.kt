package com.example.simpleweatherapp.business

import com.example.simpleweatherapp.business.model.GeoCodeModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingApi {
    @GET("geo/1.0/direct")
    fun getCityByName(
        @Query("q") name: String,
        @Query("limit") limit: String = "5",
        @Query("appid") appid: String = "f09aa410fc1c63bebcf10676a0bc8004"
    ): Observable<List<GeoCodeModel>>


    @GET("geo/1.0/reverse?")
    fun getCityByCoord(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("limit") limit: String = "10",
        @Query("appid") id: String = "f09aa410fc1c63bebcf10676a0bc8004"
    ): Observable<List<GeoCodeModel>>

}

