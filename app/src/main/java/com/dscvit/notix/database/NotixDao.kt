package com.dscvit.notix.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dscvit.notix.model.NotificationData
import com.dscvit.notix.model.TransactionData

@Dao
interface NotixDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notification: NotificationData)

    @Update
    suspend fun update(notification: NotificationData)

    @Delete
    suspend fun delete(notification: NotificationData)

    @Query(value = "Select * from notix_table ")
    fun getAllNotifications(): LiveData<List<NotificationData>>

    @Query(value = "Select * from notix_table WHERE pkgName == 'com.whatsapp' AND primaryKey IN (SELECT MAX(primaryKey) FROM notix_table GROUP BY title)")
    fun getAllUniqueConversation(): LiveData<List<NotificationData>>

    //Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransaction(transactionData: TransactionData)

    @Query(value = "Select * from transaction_table")
    fun getAllTransactions(): LiveData<List<TransactionData>>


}