package com.example.faircon.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.*
import androidx.room.Query
import com.example.faircon.models.AccountProperties

@Dao
interface AccountPropertiesDao {

    @Query("SELECT * FROM account_properties WHERE email = :email")
    suspend fun searchByEmail(email: String): AccountProperties?

    @Query("SELECT * FROM account_properties WHERE pk = :pk")
    suspend fun searchByPk(pk: Int): AccountProperties

    @Insert(onConflict = REPLACE)
    suspend fun insertAndReplace(accountProperties: AccountProperties): Long

    @Insert(onConflict = IGNORE)
    suspend fun insertOrIgnore(accountProperties: AccountProperties): Long

    @Query("UPDATE account_properties SET email = :email, username = :username WHERE pk = :pk")
    suspend fun updateAccountProperties(pk: Int, email: String, username: String)
}
