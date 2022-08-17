package com.dscvit.notix.database

import android.util.Log
import androidx.lifecycle.LiveData
import com.dscvit.notix.model.AnalyticsResponse
import com.dscvit.notix.model.NotificationData
import com.dscvit.notix.model.TransactionData
import javax.inject.Inject

class NotixRepository @Inject constructor(private val notixDao: NotixDao) {

    val allNotifications: LiveData<List<NotificationData>> = notixDao.getAllNotifications()
    val allSavedNotifications: LiveData<List<NotificationData>> = notixDao.getAllSavedNotifs()
    val allUniqueConversations: LiveData<List<NotificationData>> = notixDao.getAllUniqueConversation()
    val allSpam: LiveData<List<NotificationData>> = notixDao.getAllSpam()

    fun getTotalTodayNotifs(today: String): LiveData<List<NotificationData>> {
        return notixDao.getNumberOfNotifications(today)
    }

    fun getTotalTodaySpamNotifs(today: String): LiveData<List<NotificationData>> {
        return notixDao.getNumberOfSpamNotifications(today)
    }

    fun getTodayTopApps(today: String): LiveData<List<AnalyticsResponse>>{
        return notixDao.getTodayTopApps(today)
    }

    suspend fun insert(notificationData: NotificationData) {
        notixDao.insert(notificationData)
    }

    suspend fun delete(notificationData: NotificationData) {
        notixDao.delete(notificationData)
    }

    suspend fun update(notificationData: NotificationData) {
        notixDao.update(notificationData)
    }

    fun getAllChatsFromTitle(title: String): LiveData<List<NotificationData>> {
        return notixDao.getAllChatsFromTitle(title)
    }

    fun getLatestMessageOfTitle(title: String): LiveData<List<NotificationData>> {
        return notixDao.getNumberOfSpamNotifications(title)
    }

    //Transactions
    val allTransactions: LiveData<List<TransactionData>> = notixDao.getAllTransactions()

    suspend fun insertTransaction(transactionData: TransactionData) {
        notixDao.insertTransaction(transactionData)
    }


}