package com.example.simpleweatherapp.presenters

import android.util.Log
import com.example.simpleweatherapp.business.ApiProvider
import com.example.simpleweatherapp.business.repos.MainRepository
import com.example.simpleweatherapp.view.MainView

class MainPresenter : BasePresenter<MainView>() {

    private val repo = MainRepository(ApiProvider())

    override fun enable() {
        repo.dataEmitter
            .doAfterNext { viewState.setLoading(false) }
            .subscribe { response ->
                Log.d("MAINREPO", "Presenter enable: $response")
                viewState.displayLocation(response.cityName)
                viewState.displayCurrentData(response.weatherData)
                viewState.displayDailyData(response.weatherData.daily)
                viewState.displayHourlyData(response.weatherData.hourly)
                response.error?.let { viewState.displayError(response.error) }
            }
    }

    fun refresh(lat: String, lon: String) {
        viewState.setLoading(true)
        repo.reloadData(lat, lon)
    }
}