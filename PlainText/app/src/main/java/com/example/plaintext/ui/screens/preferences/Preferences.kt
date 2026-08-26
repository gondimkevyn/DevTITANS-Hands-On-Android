package com.example.plaintext.ui.screens.preferences

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.plaintext.ui.screens.login.TopBarComponent
import com.example.plaintext.ui.screens.util.PreferenceItem
import com.example.plaintext.ui.viewmodel.PreferencesState
import com.example.plaintext.ui.viewmodel.PreferencesViewModel

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: PreferencesViewModel = hiltViewModel()
){
    val state = viewModel.state
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopBarComponent(title = "Configurações")
        }
    ){ padding ->
        SettingsContent(
            modifier = Modifier.padding(padding),
            state = state,
            onLoginChange = { 
                viewModel.updateLogin(it)
                android.widget.Toast.makeText(context, "Login alterado com sucesso!", android.widget.Toast.LENGTH_SHORT).show()
            },
            onPasswordChange = { 
                viewModel.updatePassword(it)
                android.widget.Toast.makeText(context, "Senha alterada com sucesso!", android.widget.Toast.LENGTH_SHORT).show()
            },
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
    var isChangingAdmin by remember { mutableStateOf(false) }
    var currentLoginInput by remember { mutableStateOf("") }
    var newLoginInput by remember { mutableStateOf("") }
    var confirmLoginInput by remember { mutableStateOf("") }

    var isChangingPassword by remember { mutableStateOf(false) }
    var currentPassInput by remember { mutableStateOf("") }
    var newPassInput by remember { mutableStateOf("") }
    var confirmPassInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())){

        Text("Informações do Administrador", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Login Atual: ${state.login}", style = MaterialTheme.typography.bodyLarge)
                Text("Senha Atual: ${state.password}", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Gerenciamento de Acesso", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))

        // Seção Troca de Login
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isChangingAdmin, onCheckedChange = { isChangingAdmin = it })
            Text("Desejo alterar o login de permissão")
        }

        if (isChangingAdmin) {
            OutlinedTextField(value = currentLoginInput, onValueChange = { currentLoginInput = it }, label = { Text("Login Atual") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = newLoginInput, onValueChange = { newLoginInput = it }, label = { Text("Novo Login") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = confirmLoginInput, onValueChange = { confirmLoginInput = it }, label = { Text("Confirmar Novo Login") }, modifier = Modifier.fillMaxWidth())
            Button(
                onClick = {
                    if (currentLoginInput == state.login && newLoginInput == confirmLoginInput && newLoginInput.isNotEmpty()) {
                        onLoginChange(newLoginInput)
                        isChangingAdmin = false
                        currentLoginInput = ""; newLoginInput = ""; confirmLoginInput = ""
                    }
                },
                modifier = Modifier.align(Alignment.End).padding(top = 8.dp)
            ) { Text("Alterar Login") }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        // Seção Troca de Senha
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isChangingPassword, onCheckedChange = { isChangingPassword = it })
            Text("Desejo alterar a senha do sistema")
        }

        if (isChangingPassword) {
            OutlinedTextField(value = currentPassInput, onValueChange = { currentPassInput = it }, label = { Text("Senha Atual") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = newPassInput, onValueChange = { newPassInput = it }, label = { Text("Nova Senha") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = confirmPassInput, onValueChange = { confirmPassInput = it }, label = { Text("Confirmar Nova Senha") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            Button(
                onClick = {
                    if (currentPassInput == state.password && newPassInput == confirmPassInput && newPassInput.isNotEmpty()) {
                        onPasswordChange(newPassInput)
                        isChangingPassword = false
                        currentPassInput = ""; newPassInput = ""; confirmPassInput = ""
                    }
                },
                modifier = Modifier.align(Alignment.End).padding(top = 8.dp)
            ) { Text("Alterar Senha") }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        PreferenceItem(
            title = "Habilitar Preenchimento Automático",
            summary = "Preencher login admin automaticamente na tela inicial",
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
