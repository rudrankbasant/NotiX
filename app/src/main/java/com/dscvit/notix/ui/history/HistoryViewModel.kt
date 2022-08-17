package com.dscvit.notix.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dscvit.notix.database.NotixRepository
import com.dscvit.notix.model.NotificationData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    application: Application,
    private val repository: NotixRepository
) : AndroidViewModel(application) {

    var allNotifications = repository.allNotifications
    var allSavedNotifications = repository.allSavedNotifications
    var allSpam = repository.allSpam

    fun update(notificationData: NotificationData) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(notificationData)
    }


}