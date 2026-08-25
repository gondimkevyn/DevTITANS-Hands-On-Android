package com.example.plaintext.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class PreferencesState(
    val login: String = "devtitans",
    val password: String = "123",
    val preencher: Boolean = true
)

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    handle: SavedStateHandle,
) : ViewModel() {
    var state by mutableStateOf(PreferencesState())
        private set

    fun updateLogin(login: String) {
        state = state.copy(login = login)
    }

    fun updatePassword(password: String) {
        state = state.copy(password = password)
    }

    fun updatePreencher(preencher: Boolean) {
        state = state.copy(preencher = preencher)
    }

    fun checkCredentials(login: String, password: String): Boolean {
        return login == state.login && password == state.password
    }
}
