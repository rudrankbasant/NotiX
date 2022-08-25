package com.dscvit.notix.database

import androidx.lifecycle.LiveData
import com.dscvit.notix.model.AnalyticsResponse
import com.dscvit.notix.model.NotificationData
import com.dscvit.notix.model.TransactionData
import com.dscvit.notix.model.WhiteListData
import javax.inject.Inject

class NotixRepository @Inject constructor(private val notixDao: NotixDao) {

    val allNotifications: LiveData<List<NotificationData>> = notixDao.getAllNotifications()
    val allSavedNotifications: LiveData<List<NotificationData>> = notixDao.getAllSavedNotifs()
//    val allUniqueConversations: LiveData<List<NotificationData>> = notixDao.getAllUniqueConversation()
    val allSpam: LiveData<List<NotificationData>> = notixDao.getAllSpam()

    suspend fun getAllUniqueConversation(): List<NotificationData> =
        notixDao.getAllUniqueConversation()

    fun getTotalTodayNotifs(today: String): LiveData<List<NotificationData>> {
        return notixDao.getNumberOfNotifications(today)
    }

    fun getTotalTodaySpamNotifs(today: String): LiveData<List<NotificationData>> {
        return notixDao.getNumberOfSpamNotifications(today)
    }

    fun getTodayTopApps(today: String): LiveData<List<AnalyticsResponse>> {
        return notixDao.getTodayTopApps(today)
    }

    suspend fun getAllWhitelistedApps() = notixDao.getAllWhitelistedApps()
    suspend fun insert(notificationData: NotificationData) {
        /*val allWhitelistedApps = notixDao.getAllWhitelistedApps()
        for(i in allWhitelistedApps){
            if(notificationData.pkgName == i.pkgName){
                notificationData.spamScore = 0.0f
            }
        }*/
        notixDao.insert(notificationData)
    }

    suspend fun delete(notificationData: NotificationData) {
        notixDao.delete(notificationData)
    }

    suspend fun insertWhitelisted(whiteListData: WhiteListData){
        notixDao.insertWhitelisted(whiteListData)
    }
    suspend fun deleteWhitelisted(whiteListData: WhiteListData){
        notixDao.deleteWhitelisted(whiteListData)
    }

    suspend fun updateWhitelist(whiteListData: WhiteListData) {
        notixDao.updateWhitelist(whiteListData)
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