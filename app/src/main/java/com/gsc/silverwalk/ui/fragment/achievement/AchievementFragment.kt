package com.gsc.silverwalk.ui.fragment.achievement

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.gsc.silverwalk.MyMapViewAsync
import com.gsc.silverwalk.ui.history.HistoryActivity
import com.gsc.silverwalk.R
import com.gsc.silverwalk.userinfo.UserInfo
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.cardview_history.view.*
import kotlinx.android.synthetic.main.fragment_achievement.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class AchievementFragment : Fragment() {

    private lateinit var achievementViewModel: AchievementViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_achievement, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        achievementViewModel = ViewModelProvider(this, AchievementViewModelFactory())
            .get(AchievementViewModel::class.java)

        achievementViewModel.achievementForm.observe(viewLifecycleOwner, Observer {
            val achievementForm = it ?: return@Observer

            achievement_today_steps_text.setText(achievementForm.totalSteps.toString())
            achievement_today_averagepace_text.setText(achievementForm.totalAveragePace.toString())
            achievement_today_calories_text.setText(achievementForm.totalBurnCalories.toString())
            achievement_today_heartrate_text.setText(achievementForm.totalHeartrate.toString())
            achievement_today_distance_text.setText(achievementForm.totalDistanceStringFormat())
            achievement_today_time_text.setText(achievementForm.totalTimeStringFormat())
        })

        achievementViewModel.achievementHistoryForm.observe(viewLifecycleOwner, Observer {
            val achievementHistoryForm = it ?: return@Observer

            val iter = achievementHistoryForm.histories!!.iterator()
            while (iter.hasNext()) {
                val item = iter.next()

                val historyLayout = layoutInflater.inflate(
                    R.layout.cardview_history,
                    achievement_history_linearlayout, false
                )

                historyLayout.setOnClickListener {
                    val historyActivityIntent = Intent(context, HistoryActivity::class.java)
                    historyActivityIntent.putExtra("steps", item.steps)
                    historyActivityIntent.putExtra("averagePace", item.averagePace)
                    historyActivityIntent.putExtra("distance", item.distance)
                    historyActivityIntent.putExtra("heartRate", item.heartRate)
                    historyActivityIntent.putExtra("location", item.location)
                    historyActivityIntent.putExtra("time", item.time)
                    historyActivityIntent.putExtra("calories", item.calories)
                    historyActivityIntent.putExtra("walkTime", item.walkTime)
                    startActivity(historyActivityIntent)
                }

                historyLayout.history_cardview_mapview.onCreate(savedInstanceState)
                historyLayout.history_cardview_mapview.getMapAsync(
                    MyMapViewAsync(item.location.toString(), requireContext()))
                historyLayout.history_cardview_mapview.onResume()

                historyLayout.history_card_view_text_1.setText(item.location)
                historyLayout.history_card_view_text_2.setText(item.timeToStringFormatYYMMDD())
                historyLayout.history_card_view_text_3.setText(item.timeToStringFormatHHMMAA())

                achievement_history_linearlayout.addView(historyLayout)
            }
        })

        achievement_tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                achievementViewModel.setAchievementData(tab?.position!!)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        achievementViewModel.findAllHistory(requireContext())
    }

    override fun onResume() {
        super.onResume()
        val it = achievement_history_linearlayout.iterator()
        while(it.hasNext()){
            it.next().history_cardview_mapview.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        val it = achievement_history_linearlayout.iterator()
        while(it.hasNext()){
            it.next().history_cardview_mapview.onPause()
        }
    }
}