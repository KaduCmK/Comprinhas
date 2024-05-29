package com.example.comprinhas.ui.welcome

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprinhas.ui.theme.ComprinhasTheme

@Composable
fun SetListScreen(
    newList: Boolean,
    onLogin: (String, String) -> Unit
) {
    var listName by remember { mutableStateOf("") }
    var listPassword by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (newList) "Criar nova Lista" else "Entrar em uma Lista",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = listName,
                onValueChange = { listName = it },
                label = { Text(text = "Nome da Lista") }
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = listPassword,
                onValueChange = { listPassword = it },
                label = { Text(text = "Senha") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { onLogin(listName, listPassword) }) {
                Icon(
                    modifier = Modifier.padding(end = 4.dp),
                    imageVector = if (newList) Icons.Outlined.Add else Icons.AutoMirrored.Outlined.Login,
                    contentDescription = null
                )
                Text(text = if (newList) "Criar Lista" else "Entrar")
            }
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SetListScreenPreview() {
    ComprinhasTheme {
        SetListScreen(true, { _, _ -> })
    }
}