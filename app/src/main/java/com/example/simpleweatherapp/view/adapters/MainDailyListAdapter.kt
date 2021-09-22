package com.example.simpleweatherapp.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import butterknife.BindView
import butterknife.ButterKnife
import com.example.simpleweatherapp.R
import com.example.simpleweatherapp.business.model.DailyWeatherModel
import com.example.simpleweatherapp.view.*
import com.google.android.material.textview.MaterialTextView
import java.lang.StringBuilder

class MainDailyListAdapter : BaseAdapter<DailyWeatherModel>() {

    lateinit var clickListener: DayItemClick

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_main_daily, parent, false)
        return DailyViewHolder(view)
    }

    interface DayItemClick {
        fun showDetails(data: DailyWeatherModel)
    }

    @SuppressLint("NonConstantResourceId")
    inner class DailyViewHolder(view: View) : BaseViewHolder(view) {

        @BindView(R.id.day_container)
        lateinit var container: CardView

        @BindView(R.id.item_daily_date_tv)
        lateinit var date: MaterialTextView

        @BindView(R.id.item_daily_pop_tv)
        lateinit var popRate: MaterialTextView

        @BindView(R.id.item_daily_temp_min_tv)
        lateinit var minTemp: MaterialTextView

        @BindView(R.id.item_daily_temp_max_tv)
        lateinit var maxTemp: MaterialTextView

        @BindView(R.id.item_daily_weather_condition_icon)
        lateinit var icon: ImageView


        init {
            ButterKnife.bind(this, itemView)
        }

        override fun bindView(position: Int) {
            val itemData = mData[position]
            val defaultTextColor = date.textColors
            if (position == 0) {
                date.setTextColor(ContextCompat.getColor(date.context, R.color.purple_500))
            } else {
                date.setTextColor(defaultTextColor)
            }

            container.setOnClickListener(View.OnClickListener {
                clickListener.showDetails(itemData)
            })

            if (mData.isNotEmpty()) {
                itemData.apply {
                    val dateOfDay = dt.toDateFormatOf(DAY_WEEK_NAME_LONG)
                    date.text = if (dateOfDay.startsWith("0", true)) {
                        dateOfDay.removePrefix("0")
                    } else dateOfDay

                    if (pop < 0.01) {
                        popRate.visibility = View.VISIBLE
                    } else {
                        popRate.visibility = View.VISIBLE
                        popRate.text = pop.toPercentString("%")
                    }
                    minTemp.text = StringBuilder().append(temp.min.toDegree()).append("°")
                    maxTemp.text = StringBuilder().append(temp.max.toDegree()).append("°")
                    icon.setImageResource(weather[0].icon.provideIcon())
                }
            }
        }
    }
}
