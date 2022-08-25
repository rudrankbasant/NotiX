package com.dscvit.notix.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dscvit.notix.database.NotixRepository
import com.dscvit.notix.model.AnalyticsResponse
import com.dscvit.notix.model.NotificationData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val repository: NotixRepository
) : AndroidViewModel(application) {

    fun getTotalTodayNotifs(today: String): LiveData<List<NotificationData>> {
        return repository.getTotalTodayNotifs(today)
    }

    fun getTotalTodaySpamNotifs(today: String): LiveData<List<NotificationData>> {
        return repository.getTotalTodaySpamNotifs(today)
    }

    fun getTodayTopApps(today: String): LiveData<List<AnalyticsResponse>> {
        return repository.getTodayTopApps(today)
    }

}

