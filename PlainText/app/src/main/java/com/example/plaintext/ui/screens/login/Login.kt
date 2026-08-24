package com.example.plaintext.ui.screens.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.plaintext.R
import com.example.plaintext.ui.theme.PlainTextTheme
import com.example.plaintext.ui.viewmodel.PreferencesViewModel

data class LoginState(
    val preencher: Boolean,
    val login: String,
    val navigateToSettings: () -> Unit,
    val navigateToList: (name: String) -> Unit,
    val checkCredentials: (login: String, password: String) -> Boolean,
)


@Composable
fun Login_screen(
    navigateToSettings: () -> Unit,
    navigateToList: () -> Unit,
    viewModel: PreferencesViewModel = hiltViewModel()
) {
    val prefs = viewModel.preferencesState

    LoginContent(
        state = LoginState(
            preencher = prefs.preencher,
            login = prefs.login,
            navigateToSettings = navigateToSettings,
            navigateToList = { _ -> navigateToList() },
            checkCredentials = { login, senha -> viewModel.checkCredentials(login, senha) }
        )
    )
}


@Composable
fun LoginContent(state: LoginState) {

    val context = LocalContext.current
    var login by rememberSaveable {
        mutableStateOf(if (state.preencher) state.login else "")
    }
    var senha by rememberSaveable { mutableStateOf("") }
    var salvar by rememberSaveable { mutableStateOf(state.preencher) }

    Scaffold(
        topBar = {
            TopBarComponent(
                navigateToSettings = state.navigateToSettings,
                navigateToSensores = { }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)                        // desvia da TopBar
                .fillMaxSize()                                // ocupa a tela toda
                .background(Color(0xFF1B1B1B))                // fundo escuro do print
                .verticalScroll(rememberScrollState())        // rola com o teclado aberto
        ) {

            LoginBanner()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Digite suas credenciais para continuar",
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            CampoDeTexto(
                label = "Login:",
                value = login,
                onValueChange = { login = it }
            )

            CampoDeTexto(
                label = "Senha:",
                value = senha,
                onValueChange = { senha = it },
                isPassword = true
            )

            // A Row inteira é clicável -> alvo de toque maior.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .clickable { salvar = !salvar },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = salvar,
                    onCheckedChange = { salvar = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Salvar as informações de login",
                    color = Color.White,
                    fontSize = 15.sp
                )
            }

            Button(
                onClick = {
                    if (state.checkCredentials(login, senha)) {
                        state.navigateToList(login)
                    } else {
                        Toast.makeText(
                            context,
                            "Login ou senha inválidos",
                            Toast.LENGTH_SHORT
                        ).show()   // sem .show() o Toast NÃO aparece
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .widthIn(min = 140.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF6B183),
                    contentColor = Color(0xFF4A2A12)
                )
            ) {
                Text(text = "Enviar", fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun LoginBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF8CBF3F))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Logo do Android",
            modifier = Modifier.size(96.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text("\"The most secure", color = Color.White, fontSize = 16.sp)
            Text("password manager\"", color = Color.White, fontSize = 16.sp)
            Text("Bob and Alice", color = Color.White, fontSize = 16.sp)
        }
    }
}

@Composable
fun CampoDeTexto(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 15.sp,
            modifier = Modifier.width(70.dp)   // largura fixa alinha os dois rótulos
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            visualTransformation =
            if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            modifier = Modifier.weight(1f),    // ocupa todo o espaço restante da Row
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFFE8825A),
                unfocusedBorderColor = Color(0xFF7A7A7A),
                cursorColor = Color(0xFFE8825A)
            )
        )
    }
}



@Preview(showBackground = true, showSystemUi = true, name = "Tela de Login")
@Composable
fun LoginPreview() {
    PlainTextTheme(dynamicColor = false) {
        LoginContent(
            state = LoginState(
                preencher = true,
                login = "devtitans",
                navigateToSettings = {},
                navigateToList = {},
                checkCredentials = { _, _ -> true }
            )
        )
    }
}


@Composable
fun MyAlertDialog(shouldShowDialog: MutableState<Boolean>) {
    if (shouldShowDialog.value) {
        AlertDialog(
            onDismissRequest = { shouldShowDialog.value = false },
            title = { Text(text = "Sobre") },
            text = { Text(text = "PlainText Password Manager v1.0") },
            confirmButton = {
                Button(onClick = { shouldShowDialog.value = false }) {
                    Text(text = "Ok")
                }
            }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBarComponent(
    navigateToSettings: (() -> Unit?)? = null,
    navigateToSensores: (() -> Unit?)? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    val shouldShowDialog = remember { mutableStateOf(false) }

    if (shouldShowDialog.value) {
        MyAlertDialog(shouldShowDialog = shouldShowDialog)
    }

    TopAppBar(
        title = { Text("PlainText") },
        actions = {
            if (navigateToSettings != null && navigateToSensores != null) {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Configurações") },
                        onClick = {
                            navigateToSettings()
                            expanded = false
                        },
                        modifier = Modifier.padding(8.dp)
                    )
                    DropdownMenuItem(
                        text = { Text("Sobre") },
                        onClick = {
                            shouldShowDialog.value = true
                            expanded = false
                        },
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    )
}