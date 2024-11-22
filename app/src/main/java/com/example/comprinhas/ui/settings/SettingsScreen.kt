package com.example.comprinhas.ui.settings

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprinhas.data.AppPreferences
import com.example.comprinhas.core.presentation.TopBar
import com.example.comprinhas.ui.theme.ComprinhasTheme

@Composable
fun SettingsScreen(
    appPreferences: AppPreferences,
    updateUserPrefs: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        var name by remember { mutableStateOf(appPreferences.name) }
//        var listId by remember { mutableStateOf(appPreferences.listId) }
//        var listPassword by remember { mutableStateOf(appPreferences.listPassword) }

        Scaffold(
            topBar = {
                TopBar(
                    title = "Configurações",
                    backButton = onNavigateBack
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(64.dp))

                SettingTextField(
                    label = "Seu nome: ",
                    fieldValue = name,
                    onChange = {
                        name = it
                        updateUserPrefs(name)
                    })

//                SettingTextField(
//                    label = "Nome da lista: ",
//                    fieldValue = listId,
//                    onChange = {
//                        listId = it
//                        updateUserPrefs(name, listId, listPassword)
//                    }
//                )
//
//                SettingTextField(
//                    label = "Senha da lista:",
//                    fieldValue = listPassword,
//                    onChange = {
//                        listPassword = it
//                        updateUserPrefs(name, listId, listPassword)
//                    },
//                    isPassword = true
//                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Preview
@Composable
private fun SettingsScreenPreview() {
    ComprinhasTheme {
        SettingsScreen(
            AppPreferences(false, "",0),
            {},
            {}
        )
    }
}