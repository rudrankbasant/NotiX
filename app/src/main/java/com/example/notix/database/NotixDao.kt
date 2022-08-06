package com.example.notix.database

import android.app.Notification
import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notix.model.NotificationData
import com.example.notix.model.TransactionData
import dagger.Provides

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

    @Query(value = "Select DISTINCT * from notix_table WHERE pkgName == 'com.whatsapp'")
    fun getAllUniqueConversation(): LiveData<List<NotificationData>>

    //Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransaction(transactionData: TransactionData)

    @Query(value = "Select * from transaction_table")
    fun getAllTransactions(): LiveData<List<TransactionData>>



}