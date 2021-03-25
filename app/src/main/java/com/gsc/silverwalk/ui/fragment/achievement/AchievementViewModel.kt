package com.gsc.silverwalk.ui.fragment.achievement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.gsc.silverwalk.data.Result
import com.gsc.silverwalk.data.achievement.AchievementRepository
import java.util.*

class AchievementViewModel(private val achievementRepository: AchievementRepository) : ViewModel() {

    private val _achievementForm = MutableLiveData<AchievementForm>()
    val achievementForm: LiveData<AchievementForm> = _achievementForm

    private val _achievementHistoryForm = MutableLiveData<AchievementHistoryForm>()
    val achievementHistoryForm: LiveData<AchievementHistoryForm> = _achievementHistoryForm

    init {
        setAchievementData(0)
        findAllHistory()
    }

    fun setAchievementData(index: Int) {
        val calendar = Calendar.getInstance()
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
        val timestamp = Timestamp(Date(calendar.timeInMillis))

        achievementRepository.statisticsByTimestamp(timestamp) {
            if (it is Result.Success) {
                _achievementForm.value = it.data
            }
        }
    }

    private fun findAllHistory() {
        achievementRepository.findAllHistory {
            if (it is Result.Success) {
                _achievementHistoryForm.value = it.data
            }
        }
    }
}