package com.example.plaintext.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.data.model.toInfo
import com.example.plaintext.data.repository.PasswordDBStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ListViewState(
    var passwordList: List<PasswordInfo> = emptyList(),
    var isCollected: Boolean = false
)

@HiltViewModel
open class ListViewModel @Inject constructor (
    private val passwordDBStore: PasswordDBStore
) : ViewModel() {
    var listViewState by mutableStateOf(ListViewState())
        private set

    init {
        viewModelScope.launch {
            passwordDBStore.getList().collect { passwords ->
                listViewState = listViewState.copy(
                    passwordList = passwords.map { it.toInfo() },
                    isCollected = true
                )
            }
        }
    }

    fun savePassword(password: PasswordInfo) {
        viewModelScope.launch {
            passwordDBStore.save(password)
        }
    }
}
