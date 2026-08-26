package com.example.plaintext.ui.screens.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.plaintext.R
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.ui.screens.login.TopBarComponent
import com.example.plaintext.ui.theme.PlainTextTheme
import com.example.plaintext.ui.viewmodel.ListViewModel
import com.example.plaintext.ui.viewmodel.ListViewState
import com.example.plaintext.ui.viewmodel.PreferencesViewModel
import kotlinx.coroutines.launch

@Composable
fun ListView(
    viewModel: ListViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel(),
    navigateToEdit: (password: PasswordInfo) -> Unit,
    navigateToAdd: () -> Unit
) {
    val listState = viewModel.listViewState
    val scope = rememberCoroutineScope()
    var passwordToDelete by remember { mutableStateOf<PasswordInfo?>(null) }

    if (passwordToDelete != null) {
        DeleteConfirmationDialog(
            onConfirm = { login, pass ->
                scope.launch {
                    val isValid = preferencesViewModel.checkCredentials(login, pass)
                    if (isValid) {
                        viewModel.deletePassword(passwordToDelete!!)
                        passwordToDelete = null
                    } else {
                        // Opcional: mostrar erro no diálogo
                    }
                }
            },
            onDismiss = { passwordToDelete = null }
        )
    }

    Scaffold(
        topBar = { TopBarComponent(title = "Minhas Senhas") },
        floatingActionButton = { AddButton(onClick = navigateToAdd) }
    ) { padding ->
        ListItemContent(
            modifier = Modifier.padding(padding),
            listState = listState,
            navigateToEdit = navigateToEdit,
            onDeleteClick = { passwordToDelete = it }
        )
    }
}

@Composable
fun DeleteConfirmationDialog(
    onConfirm: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var login by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar Exclusão") },
        text = {
            Column {
                Text("Digite as credenciais de Administrador para excluir.")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = login, onValueChange = { login = it }, label = { Text("Login Admin") })
                OutlinedTextField(
                    value = pass, 
                    onValueChange = { pass = it }, 
                    label = { Text("Senha Admin") },
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(login, pass) }) { Text("Excluir") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun AddButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary
    ) {
        Icon(Icons.Filled.Add, "Small floating action button.")
    }
}

@Composable
fun ListItemContent(
    modifier: Modifier,
    listState: ListViewState,
    navigateToEdit: (password: PasswordInfo) -> Unit,
    onDeleteClick: (PasswordInfo) -> Unit
) {
    when {
        !listState.isCollected -> {
            LoadingScreen(modifier)
        }
        listState.passwordList.isEmpty() -> {
            EmptyScreen(modifier)
        }
        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                items(listState.passwordList.size) { index ->
                    ListItem(
                        listState.passwordList[index],
                        navigateToEdit,
                        onDeleteClick
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text("Carregando...")
    }
}

@Composable
fun EmptyScreen(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text("Nenhuma senha cadastrada.")
    }
}

@Composable
fun ListItem(
    password: PasswordInfo,
    navigateToEdit: (password: PasswordInfo) -> Unit,
    onDeleteClick: (PasswordInfo) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable { navigateToEdit(password) }
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Logo",
            modifier = Modifier.fillMaxHeight()
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 5.dp),
        ) {
            Text(password.name, fontSize = 20.sp)
            Text(password.login, fontSize = 14.sp)
        }
        IconButton(onClick = { onDeleteClick(password) }) {
            Icon(Icons.Default.Delete, contentDescription = "Deletar", tint = Color.Gray)
        }
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Menu",
            tint = Color.Gray
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ListViewPreview() {
    val mockPasswords = listOf(
        PasswordInfo(1, "Twitter", "dev", "123", ""),
        PasswordInfo(2, "Facebook", "devtitans", "456", ""),
        PasswordInfo(3, "Moodle", "dev.com", "789", "")
    )
    PlainTextTheme {
        Scaffold(
            topBar = { TopBarComponent(title = "Minhas Senhas") },
            floatingActionButton = { AddButton(onClick = {}) }
        ) { padding ->
            ListItemContent(
                modifier = Modifier.padding(padding),
                listState = ListViewState(passwordList = mockPasswords, isCollected = true),
                navigateToEdit = {},
                onDeleteClick = {}
            )
        }
    }
}
