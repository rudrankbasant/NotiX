package com.example.notix.database

import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import com.example.notix.model.NotificationData
import javax.inject.Inject

class NotixRepository @Inject constructor(private val notixDao: NotixDao){

    val allNotifications: LiveData<List<NotificationData>> = notixDao.getAllNotifications()

    suspend fun insert(notificationData: NotificationData){
        notixDao.insert(notificationData)
    }

    suspend fun delete(notificationData: NotificationData){
        notixDao.delete(notificationData)
    }

    suspend fun update(notificationData: NotificationData){
        notixDao.update(notificationData)
    }
}