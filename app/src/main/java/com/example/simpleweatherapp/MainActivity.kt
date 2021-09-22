package com.example.simpleweatherapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Point
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpleweatherapp.business.model.DailyWeatherModel
import com.example.simpleweatherapp.business.model.HourlyWeatherModel
import com.example.simpleweatherapp.business.model.Weather
import com.example.simpleweatherapp.business.model.WeatherDataModel
import com.example.simpleweatherapp.presenters.MainPresenter
import com.example.simpleweatherapp.view.*
import com.example.simpleweatherapp.view.adapters.MainDailyListAdapter
import com.example.simpleweatherapp.view.adapters.MainHourlyListAdapter
import com.example.simpleweatherapp.view.adapters.SettingsHolder
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.lang.StringBuilder
import kotlin.math.roundToInt


class MainActivity : MvpAppCompatActivity(), MainView {

    private val mainPresenter by moxyPresenter { MainPresenter() }

    private val geoService by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val locationRequest by lazy { initLocationRequest() }
    private lateinit var mLocation: Location

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initViews()
        initBottomSheets()
        initSwipeRefresh()

        refresh.isRefreshing = true

        supportFragmentManager.beginTransaction().add(R.id.fragment_container, DailyListFragment(), DailyListFragment::class.java.simpleName).commit()

        if (!intent.hasExtra(COORDINATES)) {
            geoService.requestLocationUpdates(locationRequest, geoCallback, null)
        } else {
            val coord = intent.extras!!.getBundle(COORDINATES)!!
            val loc = Location("")
            loc.latitude = coord.getString("lat")!!.toDouble()
            loc.longitude = coord.getString("lon")!!.toDouble()
            mLocation = loc
            mainPresenter.refresh(
                lat = mLocation.latitude.toString(),
                lon = mLocation.longitude.toString()
            )
        }

        main_menu_btn.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, android.R.anim.fade_out)
        }

        main_settings_btn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out)
        }

        main_hourly_list.apply {
            adapter = MainHourlyListAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

        mainPresenter.enable()

    }

    private fun initViews() {
//        main_city_name_tv.text = "Moscow"
//        main_date_tv.text = "1 april"
//        main_weather_condition_logo.setImageResource(R.drawable.ic_sunny_24)
//        main_weather_logo.setImageResource(R.mipmap.clowd3x)
//        main_weather_description.text = "Clear sky"
//        main_wind_speed_tv.text = "5m/s"
//        main_temp_min_tv.text = "14"
//        main_temp_max_tv.text = "22"
//        main_temp.text = "20\u00B0"
    }

    //------------ moxy code -------------

    override fun displayLocation(data: String) {
        main_city_name_tv.text = data
    }

    @SuppressLint("ResourceType")
    override fun displayCurrentData(data: WeatherDataModel) {
        data.apply {
            main_date_tv.text = current.dt.toDateFormatOf(DAY_FUll_MONTH_NAME)
            main_weather_condition_logo.setImageResource(current.weather[0].icon.provideIcon())
            main_temp.text = StringBuilder().append(current.temp.toDegree()).append("°")
            daily[0].temp.apply {
                main_temp_min_tv.text = min.toDegree()
                main_temp_max_tv.text = max.toDegree()
                main_temp_avg_tv.text = getAverage().toDegree()
            }
            main_weather_description.text = current.weather[0].description
            main_weather_logo.setImageResource(current.weather[0].icon.provideLogo())

            val pressureSet = SettingsHolder.pressure
            main_pressure_tv.text = getString(pressureSet.mesureUnitStringRes, pressureSet.getValue(current.pressure.toDouble()))

            main_humidity_tv.text = StringBuilder().append(current.humidity.toString()).append(" %")

            val windSpeedSet = SettingsHolder.windSpeed
            main_wind_speed_tv.text = getString(windSpeedSet.mesureUnitStringRes, pressureSet.getValue(current.wind_speed))

            main_sunrise_tv.text = current.sunrise.toDateFormatOf(HOUR_DOUBLE_DOT_MINUTE)
            main_sunset_tv.text = current.sunset.toDateFormatOf(HOUR_DOUBLE_DOT_MINUTE)
        }
    }

    override fun displayHourlyData(data: List<HourlyWeatherModel>) {
        (main_hourly_list.adapter as MainHourlyListAdapter).updateData(data)
    }

    override fun displayDailyData(data: List<DailyWeatherModel>) {
        (supportFragmentManager.findFragmentByTag(DailyListFragment::class.java.simpleName) as DailyListFragment).setData(data)
    }

    override fun displayError(error: Throwable) {

    }

    override fun setLoading(flag: Boolean) {
        refresh.isRefreshing = flag
    }

    //------------ moxy code -------------

    //------------ location code --------------

    private fun initLocationRequest(): LocationRequest {
        val request = LocationRequest.create()
        return request.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private val geoCallback = object : LocationCallback() {
        override fun onLocationResult(geo: LocationResult) {
            for (location in geo.locations) {
                mLocation = location
                mainPresenter.refresh(mLocation.latitude.toString(), mLocation.longitude.toString())
            }
        }
    }

    //------------ location code --------------

    private fun initBottomSheets() {
        main_bottom_sheet.isNestedScrollingEnabled = true
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        main_bottom_sheets_container.layoutParams =
            CoordinatorLayout.LayoutParams(size.x, (size.y * 0.5).roundToInt())
    }

    private fun initSwipeRefresh() {
        refresh.apply {
            setColorSchemeResources(R.color.purple_700)
            setProgressViewEndTarget(false, 280)
            setOnRefreshListener {
                mainPresenter.refresh(mLocation.latitude.toString(), mLocation.longitude.toString())
            }
        }
    }
}