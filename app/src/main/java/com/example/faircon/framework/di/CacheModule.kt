package com.example.faircon.framework.di

import androidx.room.Room
import com.example.faircon.framework.datasource.cache.dao.AccountPropertiesDao
import com.example.faircon.framework.datasource.cache.database.AppDatabase
import com.example.faircon.framework.datasource.cache.database.AppDatabase.Companion.DATABASE_NAME
import com.example.faircon.framework.datasource.cache.dao.AuthTokenDao
import com.example.faircon.framework.presentation.ui.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Provides
    fun provideAppDatabase(app: BaseApplication): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideAuthTokenDao(db: AppDatabase): AuthTokenDao {
        return db.getAuthTokenDao()
    }

    @Provides
    fun provideAccountPropertiesDao(db: AppDatabase): AccountPropertiesDao {
        return db.getAccountPropertiesDao()
    }
}