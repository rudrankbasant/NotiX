package com.example.notix.database

import android.content.Context
import android.provider.ContactsContract
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notix.model.NotificationData


@Database(entities = [NotificationData::class], version = 4, exportSchema = false)
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