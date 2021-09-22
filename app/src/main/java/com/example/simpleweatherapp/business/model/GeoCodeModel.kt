package com.example.simpleweatherapp.business.model

data class GeoCodeModel(
    val name: String,
    val local_names: LocalNames,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String?,
    var isFavorite: Boolean = false
)