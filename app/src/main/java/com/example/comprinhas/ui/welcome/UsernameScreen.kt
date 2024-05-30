package com.example.comprinhas.ui.welcome

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
fun UsernameScreen(
    onContinue: (String, Boolean) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    Surface(modifier = Modifier
        .fillMaxSize(),
        ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Bem-Vindo!", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = "Nome de Usuário") },
                isError = error,
                supportingText = {
                    if (error) Text(
                        text = "Nome de usuário inválido",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.width(235.dp),
                onClick = {
                    if (name.isEmpty()) error = true
                    else onContinue(name, false)
                }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Login,
                        contentDescription = "Lista Existente"
                    )
                    Text(modifier = Modifier.padding(2.dp), text = "Entrar em Lista existente")
                }
            }

            Button(
                modifier = Modifier.width(235.dp),
                onClick = {
                    if (name.isEmpty()) error = true
                    else onContinue(name, true)
                }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "Nova Lista"
                    )
                    Text(modifier = Modifier.padding(2.dp), text = "Criar nova Lista")
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun WelcomeScreenPreview() {
    ComprinhasTheme {
        UsernameScreen { _, _ ->}
    }
}