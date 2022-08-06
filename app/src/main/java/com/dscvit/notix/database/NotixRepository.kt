package com.dscvit.notix.database

import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import com.dscvit.notix.model.NotificationData
import com.dscvit.notix.model.TransactionData
import javax.inject.Inject

class NotixRepository @Inject constructor(private val notixDao: NotixDao) {

    val allNotifications: LiveData<List<NotificationData>> = notixDao.getAllNotifications()
    val allUniqueConversations: LiveData<List<NotificationData>> =
        notixDao.getAllUniqueConversation()

    suspend fun insert(notificationData: NotificationData) {
        notixDao.insert(notificationData)
    }

    suspend fun delete(notificationData: NotificationData) {
        notixDao.delete(notificationData)
    }

    suspend fun update(notificationData: NotificationData) {
        notixDao.update(notificationData)
    }

    //Transactions
    val allTransactions: LiveData<List<TransactionData>> = notixDao.getAllTransactions()

    suspend fun insertTransaction(transactionData: TransactionData) {
        notixDao.insertTransaction(transactionData)
    }

}