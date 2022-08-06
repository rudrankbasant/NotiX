package com.dscvit.notix.di

import android.content.Context
import androidx.room.Room
import com.dscvit.notix.database.NotixDao
import com.dscvit.notix.database.NotixDatabase
import com.dscvit.notix.database.NotixRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {


    @Provides
    @Singleton
    fun provideNotixRepository(
        notixDao: NotixDao,
    ): NotixRepository {
        return NotixRepository(
            notixDao
        )
    }

    @Provides
    fun providesNotixDao(appDatabase: NotixDatabase): NotixDao {
        return appDatabase.getNotificationsDao()
    }

    @Provides
    @Singleton
    fun provideNotixDatabase(@ApplicationContext context: Context): NotixDatabase {

        synchronized(this) {
            return Room.databaseBuilder(
                context,
                NotixDatabase::class.java,
                "notix_database"
            ).fallbackToDestructiveMigration().build()
        }
    }

}