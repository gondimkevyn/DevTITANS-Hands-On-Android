package com.example.plaintext.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class AppSettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val LOGIN_KEY = stringPreferencesKey("admin_login")
    private val PASSWORD_KEY = stringPreferencesKey("admin_password")
    private val AUTOFILL_KEY = booleanPreferencesKey("enable_autofill")

    val adminLogin: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LOGIN_KEY] ?: "devtitans"
    }

    val adminPassword: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PASSWORD_KEY] ?: "123"
    }

    val enableAutofill: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[AUTOFILL_KEY] ?: true
    }

    suspend fun updateAdminLogin(login: String) {
        context.dataStore.edit { preferences ->
            preferences[LOGIN_KEY] = login
        }
    }

    suspend fun updateAdminPassword(password: String) {
        context.dataStore.edit { preferences ->
            preferences[PASSWORD_KEY] = password
        }
    }

    suspend fun updateEnableAutofill(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AUTOFILL_KEY] = enabled
        }
    }
}
