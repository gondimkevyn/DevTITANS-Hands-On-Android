package com.example.plaintext.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.plaintext.data.model.PasswordInfo
import com.example.plaintext.ui.screens.editList.EditList
import com.example.plaintext.ui.screens.hello.Hello_screen
import com.example.plaintext.ui.screens.list.ListView
import com.example.plaintext.ui.screens.login.Login_screen
import com.example.plaintext.ui.screens.preferences.SettingsScreen
import com.example.plaintext.ui.viewmodel.ListViewModel
import com.example.plaintext.utils.parcelableType
import kotlin.reflect.typeOf

@Composable
fun PlainTextApp(
    appState: JetcasterAppState = rememberJetcasterAppState()
) {
    NavHost(
        navController = appState.navController,
        startDestination = Screen.Login,
    )
    {
        composable<Screen.Hello>{
            val args = it.toRoute<Screen.Hello>()
            Hello_screen(args)
        }
        composable<Screen.Login>{
            Login_screen(
                navigateToSettings = { appState.navigateToPreferences() },
                navigateToList = { appState.navigateToList() }
            )
        }
        composable<Screen.List> {
            ListView(
                navigateToEdit = { password ->
                    appState.navigateToEditList(password)
                },
                navigateToAdd = {
                    appState.navigateToEditList(PasswordInfo(0, "", "", "", ""))
                }
            )
        }
        composable<Screen.Preferences> {
            SettingsScreen(
                onBackClick = { appState.navController.popBackStack() }
            )
        }
        composable<Screen.EditList>(
            typeMap = mapOf(typeOf<PasswordInfo>() to parcelableType<PasswordInfo>())
        ) {
            val args = it.toRoute<Screen.EditList>()
            val viewModel: ListViewModel = hiltViewModel()
            EditList(
                args,
                navigateBack = { appState.navController.popBackStack() },
                savePassword = { password -> viewModel.savePassword(password) }
            )
        }
    }
}
