package com.example.plaintext.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.plaintext.data.model.Password
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PasswordDao : BaseDao<Password> {
    @Query("SELECT * FROM passwords")
    abstract fun getAll(): Flow<List<Password>>

    @Query("SELECT * FROM passwords WHERE id = :id")
    abstract suspend fun getById(id: Int): Password?

    @Query("SELECT (count(*) = 0) FROM passwords")
    abstract fun isEmpty(): Flow<Boolean>

    @Query("SELECT * FROM passwords WHERE login = :login AND password = :password LIMIT 1")
    abstract suspend fun checkCredentials(login: String, password: String): Password?
}
