package com.example.simpleweatherapp.view

import com.example.simpleweatherapp.business.model.GeoCodeModel
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface MenuView: MvpView {

    @AddToEndSingle
    fun setLoading(flag: Boolean)

    @AddToEndSingle
    fun fillPredicationList(data: List<GeoCodeModel>)

    @AddToEndSingle
    fun fillFavouriteList(data: List<GeoCodeModel>)
}