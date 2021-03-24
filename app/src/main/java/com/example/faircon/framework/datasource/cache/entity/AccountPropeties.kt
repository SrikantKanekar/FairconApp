package com.example.faircon.framework.datasource.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_properties")
data class AccountProperties(
    @PrimaryKey(autoGenerate = false)
    var pk: Int,

    var email: String,

    var username: String
) {

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false
        other as AccountProperties
        if (pk != other.pk) return false
        if (email != other.email) return false
        if (username != other.username) return false
        return true
    }

    override fun hashCode(): Int {
        var result = pk
        result = 31 * result + email.hashCode()
        result = 31 * result + username.hashCode()
        return result
    }
}
