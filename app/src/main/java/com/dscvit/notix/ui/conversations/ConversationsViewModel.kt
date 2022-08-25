package com.dscvit.notix.ui.conversations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dscvit.notix.database.NotixRepository
import com.dscvit.notix.model.NotificationData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationsViewModel @Inject constructor(
    application: Application,
    private val repository: NotixRepository
) : AndroidViewModel(application) {

    private val _allUniqueConversation = MutableLiveData<List<NotificationData>>(emptyList())
    val allUniqueConversation : LiveData<List<NotificationData>> = _allUniqueConversation

//    val allUniqueConversation = repository.allUniqueConversations

    init {
        getUniqueConversation()
    }

    fun getUniqueConversation() {
        viewModelScope.launch {
            _allUniqueConversation.postValue(repository.getAllUniqueConversation())
        }
    }

    fun getAllChatsFromTitle(title: String): LiveData<List<NotificationData>> {
        return repository.getAllChatsFromTitle(title)
    }
}