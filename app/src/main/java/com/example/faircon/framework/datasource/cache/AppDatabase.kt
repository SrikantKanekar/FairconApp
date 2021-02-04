package com.example.faircon.framework.datasource.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.faircon.framework.datasource.cache.auth.AuthTokenDao
import com.example.faircon.framework.datasource.cache.main.model.AccountProperties
import com.example.faircon.framework.datasource.cache.auth.models.AuthToken
import com.example.faircon.framework.datasource.cache.main.AccountPropertiesDao

@Database(entities = [AuthToken::class, AccountProperties::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getAuthTokenDao(): AuthTokenDao

    abstract fun getAccountPropertiesDao(): AccountPropertiesDao

    companion object{
        const val DATABASE_NAME: String = "DATABASE_NAME"
    }
}
