package com.example.plaintext.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.plaintext.data.repository.PasswordDBStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class LoginViewState(
    val login: String = "",
    val password: String = "",
    val rememberMe: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val passwordStore: PasswordDBStore
) : ViewModel() {
    var state by mutableStateOf(LoginViewState())
        private set

    fun onLoginChanged(newValue: String) {
        state = state.copy(login = newValue)
    }

    fun onPasswordChanged(newValue: String) {
        state = state.copy(password = newValue)
    }

    fun onRememberMeChanged(newValue: Boolean) {
        state = state.copy(rememberMe = newValue)
    }

    suspend fun validateInDatabase(login: String, pass: String): Boolean {
        return passwordStore.checkCredentials(login, pass)
    }
}
