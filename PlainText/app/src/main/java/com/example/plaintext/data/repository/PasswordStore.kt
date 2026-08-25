package com.example.plaintext.data.repository

import com.example.plaintext.data.dao.PasswordDao
import com.example.plaintext.data.model.Password
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.data.model.toEntity
import kotlinx.coroutines.flow.Flow

interface PasswordDBStore {
    fun getList(): Flow<List<Password>>
    suspend fun add(password: Password): Long
    suspend fun update(password: Password)
    suspend fun get(id: Int): Password?
    suspend fun save(passwordInfo: PasswordInfo)
    fun isEmpty(): Flow<Boolean>
    suspend fun checkCredentials(login: String, password: String): Boolean
}

class LocalPasswordDBStore(
    private val passwordDao : PasswordDao
): PasswordDBStore {
    override fun getList(): Flow<List<Password>> {
        return passwordDao.getAll()
    }

    override suspend fun add(password: Password): Long {
        return passwordDao.insert(password)
    }

    override suspend fun update(password: Password) {
        passwordDao.update(password)
    }

    override suspend fun get(id: Int): Password? {
        return passwordDao.getById(id)
    }

    override suspend fun save(passwordInfo: PasswordInfo) {
        val entity = passwordInfo.toEntity()
        if (entity.id == 0) {
            passwordDao.insert(entity)
        } else {
            passwordDao.update(entity)
        }
    }

    override fun isEmpty(): Flow<Boolean> {
        return passwordDao.isEmpty()
    }

    override suspend fun checkCredentials(login: String, password: String): Boolean {
        return passwordDao.checkCredentials(login, password) != null
    }
}
