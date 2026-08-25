package com.example.plaintext.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.plaintext.R
import com.example.plaintext.ui.theme.PlainTextTheme
import com.example.plaintext.ui.viewmodel.LoginViewModel
import com.example.plaintext.ui.viewmodel.PreferencesViewModel

@Composable
fun Login_screen(
    navigateToSettings: () -> Unit,
    navigateToList: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {
    val loginState = loginViewModel.state
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Login_screen_content(
        state = loginState,
        onLoginChange = { loginViewModel.onLoginChanged(it) },
        onPasswordChange = { loginViewModel.onPasswordChanged(it) },
        onRememberMeChange = { loginViewModel.onRememberMeChanged(it) },
        navigateToSettings = navigateToSettings,
        navigateToList = {
            val isAdminValid = preferencesViewModel.checkCredentials(
                loginState.login,
                loginState.password
            )
            
            if (isAdminValid) {
                navigateToList()
            } else {
                scope.launch {
                    val isUserValid = loginViewModel.validateInDatabase(
                        loginState.login,
                        loginState.password
                    )
                    if (isUserValid) {
                        navigateToList()
                    } else {
                        android.widget.Toast.makeText(
                            context,
                            "Login ou Senha incorretos!",
                            android.widget.Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    )
}

@Composable
fun Login_screen_content(
    state: com.example.plaintext.ui.viewmodel.LoginViewState,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToList: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBarComponent(
                navigateToSettings = navigateToSettings
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Logo",
                    modifier = Modifier.height(120.dp)
                )
                Text("PlainText", fontSize = 32.sp, style = MaterialTheme.typography.headlineLarge)
            }
            
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = state.login,
                onValueChange = onLoginChange,
                label = { Text("Login") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                label = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.rememberMe,
                    onCheckedChange = onRememberMeChange
                )
                Text("Lembrar-me")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navigateToList() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar")
            }
        }
    }
}

@Composable
fun MyAlertDialog(shouldShowDialog: MutableState<Boolean>) {
    if (shouldShowDialog.value) {
        AlertDialog(
            onDismissRequest = {
                shouldShowDialog.value = false
            },
            title = { Text(text = "Sobre") },
            text = { Text(text = "PlainText Password Manager v1.0") },
            confirmButton = {
                Button(
                    onClick = { shouldShowDialog.value = false }
                ) {
                    Text(text = "Ok")
                }
            }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBarComponent(
    title: String = "PlainText",
    navigateToSettings: (() -> Unit)? = null,
    navigateToSensores: (() -> Unit)? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    val shouldShowDialog = remember { mutableStateOf(false) }

    if (shouldShowDialog.value) {
        MyAlertDialog(shouldShowDialog = shouldShowDialog)
    }

    TopAppBar(
        title = { Text(title) },
        actions = {
            if (navigateToSettings != null || navigateToSensores != null) {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    if (navigateToSettings != null) {
                        DropdownMenuItem(
                            text = { Text("Configurações") },
                            onClick = {
                                navigateToSettings()
                                expanded = false
                            }
                        )
                    }
                    if (navigateToSensores != null) {
                        DropdownMenuItem(
                            text = { Text("Sensores") },
                            onClick = {
                                navigateToSensores()
                                expanded = false
                            }
                        )
                    }
                    DropdownMenuItem(
                        text = { Text("Sobre") },
                        onClick = {
                            shouldShowDialog.value = true
                            expanded = false
                        }
                    )
                }
            }
        }
    )
}

@Preview(name = "Light Mode", showBackground = true, showSystemUi = true, device = Devices.PIXEL_7)
@Preview(name = "Dark Mode", showBackground = true, showSystemUi = true, device = Devices.PIXEL_7, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginPreview() {
    PlainTextTheme {
        Login_screen_content(
            state = com.example.plaintext.ui.viewmodel.LoginViewState(),
            onLoginChange = {},
            onPasswordChange = {},
            onRememberMeChange = {},
            navigateToSettings = {},
            navigateToList = {}
        )
    }
}

