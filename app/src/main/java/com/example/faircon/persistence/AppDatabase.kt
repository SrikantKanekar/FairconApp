package com.example.faircon.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.faircon.models.AccountProperties
import com.example.faircon.models.AuthToken

@Database(entities = [AuthToken::class, AccountProperties::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getAuthTokenDao(): AuthTokenDao

    abstract fun getAccountPropertiesDao(): AccountPropertiesDao

    companion object{
        val DATABASE_NAME: String = "app_db"
    }
}
