package com.example.plaintext.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plaintext.data.repository.AppSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PreferencesState(
    val login: String = "devtitans",
    val password: String = "123",
    val preencher: Boolean = true
)

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val repository: AppSettingsRepository
) : ViewModel() {
    var state by mutableStateOf(PreferencesState())
        private set

    init {
        viewModelScope.launch {
            // Coletar valores iniciais e observar mudanças
            repository.adminLogin.collect { login ->
                state = state.copy(login = login)
            }
        }
        viewModelScope.launch {
            repository.adminPassword.collect { pass ->
                state = state.copy(password = pass)
            }
        }
        viewModelScope.launch {
            repository.enableAutofill.collect { autofill ->
                state = state.copy(preencher = autofill)
            }
        }
    }

    fun updateLogin(login: String) {
        viewModelScope.launch {
            repository.updateAdminLogin(login)
        }
    }

    fun updatePassword(password: String) {
        viewModelScope.launch {
            repository.updateAdminPassword(password)
        }
    }

    fun updatePreencher(preencher: Boolean) {
        viewModelScope.launch {
            repository.updateEnableAutofill(preencher)
        }
    }

    suspend fun checkCredentials(login: String, password: String): Boolean {
        val currentLogin = repository.adminLogin.first()
        val currentPass = repository.adminPassword.first()
        return login == currentLogin && password == currentPass
    }
}
