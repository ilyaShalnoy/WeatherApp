package com.example.simpleweatherapp.presenters

import android.util.Log
import com.example.simpleweatherapp.business.ApiProvider
import com.example.simpleweatherapp.business.model.GeoCodeModel
import com.example.simpleweatherapp.business.repos.MenuRepository
import com.example.simpleweatherapp.business.repos.SAVED
import com.example.simpleweatherapp.view.MenuView

class MenuPresenter : BasePresenter<MenuView>() {

    private val repo = MenuRepository(ApiProvider())

    override fun enable() {
        repo.dataEmitter.subscribe {
            viewState.setLoading(false)
            if(it.purpose == SAVED) {
                Log.d("123321", "enable: SAVED ${it.data}")
                viewState.fillFavouriteList(it.data)
            } else {
                Log.d("123321", "enable: SAVED ${it.data}")
                viewState.fillPredicationList(it.data)
            }
        }
    }

    fun searchFor(str: String) {
        repo.getCities(str)
    }

    fun removeLocation(data: GeoCodeModel) {
        repo.remove(data)
    }

    fun saveLocation(data: GeoCodeModel) {
        repo.add(data)
    }

    fun getFavoriteList() {
        repo.updateFavorite()
    }
}