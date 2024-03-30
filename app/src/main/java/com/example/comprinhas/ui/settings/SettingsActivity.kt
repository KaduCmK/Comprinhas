package com.example.comprinhas.ui.settings

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comprinhas.ComprinhasViewModel
import com.example.comprinhas.ui.theme.ComprinhasTheme

class SettingsActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            ComprinhasTheme {
                SettingsScreen()
            }
        }
    }
}

@Composable
fun SettingsScreen(
    mainViewModel: ComprinhasViewModel = viewModel()
) {
    val activity = LocalContext.current as Activity

    var name by remember { mutableStateOf(mainViewModel.appPreferences.name) }
    var listId by remember { mutableStateOf(mainViewModel.appPreferences.listId) }

    Scaffold(
        topBar = { SettingsTopBar { activity.finish() } }
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
                    mainViewModel.updateNameAndListId(name, listId)
                })

            SettingTextField(
                label = "ID da lista: ",
                fieldValue = listId,
                onChange = {
                    listId = it
                    mainViewModel.updateNameAndListId(name, listId)
                })
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview
@Composable
private fun SettingsScreenPreview() {
    ComprinhasTheme {
        SettingsScreen()
    }
}