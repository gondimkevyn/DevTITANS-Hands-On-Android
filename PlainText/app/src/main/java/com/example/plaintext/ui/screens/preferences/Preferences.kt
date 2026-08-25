package com.example.plaintext.ui.screens.preferences

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.plaintext.ui.screens.login.TopBarComponent
import com.example.plaintext.ui.screens.util.PreferenceInput
import com.example.plaintext.ui.screens.util.PreferenceItem
import com.example.plaintext.ui.viewmodel.PreferencesState
import com.example.plaintext.ui.viewmodel.PreferencesViewModel

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: PreferencesViewModel = hiltViewModel()
){
    val state = viewModel.state

    Scaffold(
        topBar = {
            TopBarComponent(title = "Configurações")
        }
    ){ padding ->
        SettingsContent(
            modifier = Modifier.padding(padding),
            state = state,
            onLoginChange = { viewModel.updateLogin(it) },
            onPasswordChange = { viewModel.updatePassword(it) },
            onPreencherChange = { viewModel.updatePreencher(it) }
        )
    }
}

@Composable
fun SettingsContent(
    modifier: Modifier = Modifier,
    state: PreferencesState,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPreencherChange: (Boolean) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())){

        PreferenceInput(
            title = "Preencher Login",
            label = "Login",
            fieldValue = state.login,
            summary = "Preencher login na tela inicial"
        ){
            onLoginChange(it)
        }

        PreferenceInput(
            title = "Setar Senha",
            label = "Senha",
            fieldValue = state.password,
            summary = "Senha para entrar no sistema"
        ){
            onPasswordChange(it)
        }

        PreferenceItem(
            title = "Habilitar Preenchimento",
            summary = "Ativar preenchimento automático",
            onClick = {
                onPreencherChange(!state.preencher)
            },
            control = {
                Switch(
                    checked = state.preencher,
                    onCheckedChange = {
                        onPreencherChange(it)
                    }
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsContent(
        state = PreferencesState(),
        onLoginChange = {},
        onPasswordChange = {},
        onPreencherChange = {}
    )
}
