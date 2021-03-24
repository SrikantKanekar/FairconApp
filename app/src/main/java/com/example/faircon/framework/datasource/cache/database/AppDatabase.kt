package com.example.faircon.framework.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.faircon.framework.datasource.cache.dao.AuthTokenDao
import com.example.faircon.framework.datasource.cache.entity.AccountProperties
import com.example.faircon.framework.datasource.cache.entity.AuthToken
import com.example.faircon.framework.datasource.cache.dao.AccountPropertiesDao

@Database(entities = [AuthToken::class, AccountProperties::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getAuthTokenDao(): AuthTokenDao

    abstract fun getAccountPropertiesDao(): AccountPropertiesDao

    companion object{
        const val DATABASE_NAME: String = "DATABASE_NAME"
    }
}
