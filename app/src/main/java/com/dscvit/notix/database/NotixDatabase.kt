package com.dscvit.notix.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dscvit.notix.model.NotificationData
import com.dscvit.notix.model.TransactionData
import com.dscvit.notix.model.WhiteListData


@Database(
    entities = [NotificationData::class, TransactionData::class, WhiteListData::class],
    version = 2,
    exportSchema = false
)
abstract class NotixDatabase : RoomDatabase() {


    abstract fun getNotificationsDao(): NotixDao

    /*companion object{

        @Volatile
        private var INSTANCE: NotixDatabase?=null

        fun getDatabase(context: Context): NotixDatabase {
            return INSTANCE ?: synchronized(this ){
                val instance= Room.databaseBuilder(
                    context.applicationContext,
                    NotixDatabase::class.java,
                    "notix_database"
                ).fallbackToDestructiveMigration().
                build()
                INSTANCE = instance
                instance
            }
        }

    }*/
}