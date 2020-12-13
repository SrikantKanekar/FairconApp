package com.example.faircon.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

const val AUTH_TOKEN_BUNDLE_KEY = "AUTH_TOKEN_BUNDLE_KEY"

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
@Parcelize
data class AuthToken(

    @PrimaryKey
    var account_pk: Int? = -1,

    var token: String? = null
) : Parcelable
