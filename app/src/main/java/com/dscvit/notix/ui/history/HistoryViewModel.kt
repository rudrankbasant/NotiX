package com.dscvit.notix.ui.history

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dscvit.notix.database.NotixRepository
import com.dscvit.notix.model.NotificationData
import com.dscvit.notix.model.WhiteListData
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

    private val _allWhitelisted = MutableLiveData<List<WhiteListData>>(emptyList())
    var allWhitelistedApps : LiveData<List<WhiteListData>> = _allWhitelisted

    init {
        getAllWhitelistedApps()
    }

    private fun getAllWhitelistedApps() {
        viewModelScope.launch {
            _allWhitelisted.postValue(repository.getAllWhitelistedApps())
        }
    }
    fun update(notificationData: NotificationData) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(notificationData)
    }
    fun updateWhitelist(whiteListData: WhiteListData) = viewModelScope.launch(Dispatchers.IO ){
        repository.updateWhitelist(whiteListData)
    }

    suspend fun insertWhitelisted(whiteListData: WhiteListData){
        repository.insertWhitelisted(whiteListData)
    }
    suspend fun deleteWhitelisted(whiteListData: WhiteListData){
        repository.deleteWhitelisted(whiteListData)
    }
}