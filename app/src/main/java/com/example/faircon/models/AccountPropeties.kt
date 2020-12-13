package com.example.faircon.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.faircon.ui.auth.state.LoginFields
import com.example.faircon.ui.auth.state.RegistrationFields
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "account_properties")
data class AccountProperties(
    @PrimaryKey(autoGenerate = false)
    var pk: Int,

    var email: String,

    var username: String
) : Parcelable {

    class UpdateAccountError {
        companion object {
            fun mustFillAllFields(): String {
                return "All fields are required."
            }

            fun none(): String {
                return "None"
            }
        }
    }

    fun isValidForUpdating(): String {
        if (email.isEmpty()
            || username.isEmpty()
        ) {
            return UpdateAccountError.mustFillAllFields()
        }
        return UpdateAccountError.none()
    }

    class ChangePasswordError {
        companion object {
            fun mustFillAllFields(): String {
                return "All fields are required."
            }

            fun passwordsDoNotMatch(): String {
                return "New Passwords must match."
            }

            fun none(): String {
                return "None"
            }
        }
    }

    companion object {
        fun isValidForChangingPassword(
            currentPassword: String,
            newPassword: String,
            confirmNewPassword: String
        ): String {
            if (currentPassword.isEmpty()
                || newPassword.isEmpty()
                || confirmNewPassword.isEmpty()
            ) {
                return ChangePasswordError.mustFillAllFields()
            }
            if (newPassword != confirmNewPassword) {
                return ChangePasswordError.passwordsDoNotMatch()
            }
            return ChangePasswordError.none()
        }
    }

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
