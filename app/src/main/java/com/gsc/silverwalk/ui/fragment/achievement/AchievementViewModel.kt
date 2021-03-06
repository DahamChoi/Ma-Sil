package com.gsc.silverwalk.ui.fragment.achievement

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.gsc.silverwalk.data.Result
import com.gsc.silverwalk.data.achievement.AchievementRepository
import java.time.ZoneId
import java.util.*

class AchievementViewModel(private val achievementRepository: AchievementRepository) : ViewModel() {

    private val _achievementForm = MutableLiveData<AchievementForm>()
    val achievementForm: LiveData<AchievementForm> = _achievementForm

    private val _achievementHistoryForm = MutableLiveData<AchievementHistoryForm>()
    val achievementHistoryForm: LiveData<AchievementHistoryForm> = _achievementHistoryForm

    @RequiresApi(Build.VERSION_CODES.O)
    fun setAchievementData(index: Int) {
        if(_achievementHistoryForm.value == null) return

        val calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault()))
        // Today
        if (index == 0) {
            calendar.add(Calendar.DATE, -1)
        }
        // Weekly
        else if (index == 1) {
            calendar.add(Calendar.DATE, -7)
        }
        // Monthly
        else if (index == 2) {
            calendar.add(Calendar.MONTH, -1)
        }
        // Yearly
        else {
            calendar.add(Calendar.YEAR, -1)
        }

        achievementRepository.statisticsByTimestamp(calendar.time.time, _achievementHistoryForm.value!!) {
            if (it is Result.Success) {
                _achievementForm.value = it.data
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun findAllHistory(context: Context) {
        achievementRepository.findAllHistory(context) {
            if (it is Result.Success) {
                _achievementHistoryForm.value = it.data
                setAchievementData(0)
            }
        }
    }
}