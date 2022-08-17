package com.dscvit.notix.ui.conversations

import android.app.Application
import android.icu.text.CaseMap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dscvit.notix.database.NotixRepository
import com.dscvit.notix.model.NotificationData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConversationsViewModel @Inject constructor(
    application: Application,
    private val repository: NotixRepository
) : AndroidViewModel(application) {

    val allUniqueConversation = repository.allUniqueConversations

    fun getAllChatsFromTitle(title: String): LiveData<List<NotificationData>>{
        return repository.getAllChatsFromTitle(title)
    }
}