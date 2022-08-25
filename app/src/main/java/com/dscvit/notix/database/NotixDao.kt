package com.dscvit.notix.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dscvit.notix.model.AnalyticsResponse
import com.dscvit.notix.model.NotificationData
import com.dscvit.notix.model.TransactionData
import com.dscvit.notix.model.WhiteListData

@Dao
interface NotixDao {

    //Notification Listener
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notification: NotificationData)

    //Saved
    @Update
    suspend fun update(notification: NotificationData)

    @Query(value = "Select * from notix_table WHERE saved = 1")
    fun getAllSavedNotifs(): LiveData<List<NotificationData>>


    @Delete
    suspend fun delete(notification: NotificationData)


    //History
    @Query(value = "Select * from notix_table")
    fun getAllNotifications(): LiveData<List<NotificationData>>

    //Conversations
    @Query(value = "Select * from notix_table WHERE pkgName == 'com.whatsapp' AND primaryKey IN (SELECT MAX(primaryKey) FROM notix_table GROUP BY title )")
    suspend fun getAllUniqueConversation(): List<NotificationData>

    @Query(value = "Select * from notix_table WHERE title = :title AND pkgName == 'com.whatsapp'")
    fun getAllChatsFromTitle(title: String): LiveData<List<NotificationData>>

    //Spam
    @Query(value = "Select * from notix_table WHERE spamScore >= 0.8")
    fun getAllSpam(): LiveData<List<NotificationData>>

    //Analytics
    @Query(value = "SELECT * FROM notix_table WHERE postedDate = :today")
    fun getNumberOfNotifications(today: String): LiveData<List<NotificationData>>

    @Query(value = "SELECT * FROM notix_table WHERE postedDate = :today AND spamScore>=0.8")
    fun getNumberOfSpamNotifications(today: String): LiveData<List<NotificationData>>

    @Query(value = "SELECT pkgName,COUNT(pkgName) myCount FROM notix_table WHERE postedDate = :today  GROUP BY pkgName  ORDER BY count(pkgName) DESC")
    fun getTodayTopApps(today: String): LiveData<List<AnalyticsResponse>>


    //Whitelist
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWhitelisted(whiteListData: WhiteListData)
    @Delete
    suspend fun deleteWhitelisted(whiteListData: WhiteListData)
    @Query(value = "SELECT * FROM whitelist_table")
    suspend fun getAllWhitelistedApps(): List<WhiteListData>
    @Update
    suspend fun updateWhitelist(whiteListData: WhiteListData)


    //Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransaction(transactionData: TransactionData)

    @Query(value = "Select * from transaction_table")
    fun getAllTransactions(): LiveData<List<TransactionData>>


}