package com.example.faircon.framework.datasource.cache.auth.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.example.faircon.framework.datasource.cache.main.model.AccountProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "auth_token",
    foreignKeys = [
        ForeignKey(
            entity = AccountProperties::class,
            parentColumns = ["pk"],
            childColumns = ["account_pk"],
            onDelete = CASCADE
        )
    ]
)
data class AuthToken(

    @PrimaryKey
    var account_pk: Int? = -1,

    var token: String? = null
) : Parcelable