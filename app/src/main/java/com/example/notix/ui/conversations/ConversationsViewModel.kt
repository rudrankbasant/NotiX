package com.example.notix.ui.conversations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.notix.database.NotixRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConversationsViewModel @Inject constructor(
    application: Application,
    private val repository: NotixRepository
): AndroidViewModel(application) {

    val allUniqueConversation = repository.allUniqueConversations
}