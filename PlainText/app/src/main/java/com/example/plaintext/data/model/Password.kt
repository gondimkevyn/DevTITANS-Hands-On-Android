package com.example.plaintext.data.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Entity(
    tableName = "passwords"
)
@Immutable
data class Password(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "login") val login: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "notes") val notes: String? = null,
)

@Serializable
@Parcelize
data class PasswordInfo(
    val id: Int = 0,
    val name: String,
    val login: String,
    val password: String,
    val notes: String? = null,
) : Parcelable

fun Password.toInfo() = PasswordInfo(
    id = id,
    name = name,
    login = login,
    password = password,
    notes = notes
)

fun PasswordInfo.toEntity() = Password(
    id = id,
    name = name,
    login = login,
    password = password,
    notes = notes
)
