package com.example.plaintext.ui.screens.editList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.ui.screens.Screen
import com.example.plaintext.ui.screens.login.TopBarComponent

@Composable
fun EditList(
    args: Screen.EditList,
    navigateBack: () -> Unit,
    savePassword: (password: PasswordInfo) -> Unit
) {
    val password = args.password
    val nomeState = rememberSaveable { mutableStateOf(password.name) }
    val usuarioState = rememberSaveable { mutableStateOf(password.login) }
    val senhaState = rememberSaveable { mutableStateOf(password.password) }
    val notasState = rememberSaveable { mutableStateOf(password.notes) }

    Scaffold(
        topBar = { TopBarComponent(title = if (password.id == 0) "Adicionar Senha" else "Editar Senha") }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            EditInput("Nome", nomeState)
            EditInput("Usuário/Login", usuarioState)
            EditInput("Senha", senhaState)
            EditInput("Notas", notasState, textInputHeight = 120)

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    savePassword(
                        PasswordInfo(
                            id = password.id,
                            name = nomeState.value,
                            login = usuarioState.value,
                            password = senhaState.value,
                            notes = notasState.value
                        )
                    )
                    navigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar")
            }
        }
    }
}

@Composable
fun EditInput(
    textInputLabel: String,
    textInputState: MutableState<String>,
    textInputHeight: Int = 60
) {
    var textState by textInputState

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        OutlinedTextField(
            value = textState,
            onValueChange = { textState = it },
            label = { Text(textInputLabel) },
            modifier = Modifier
                .height(textInputHeight.dp)
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditListPreview() {
    EditList(
        Screen.EditList(PasswordInfo(1, "Nome", "Usuário", "Senha", "Notas")),
        navigateBack = {},
        savePassword = {}
    )
}
