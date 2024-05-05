package com.example.comprinhas.ui.welcome

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
fun WelcomeScreen(
    onContinue: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var listId by remember { mutableStateOf("") }

    Surface(modifier = Modifier
        .fillMaxSize(),
        ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Insira seu nome e id da lista", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = "Nome")}
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = listId,
                onValueChange = { listId = it },
                label = { Text(text = "Id da Lista")}
            )
            Button(
                modifier = Modifier.padding(top = 64.dp),
                onClick = { onContinue(name, listId) }
            ) {
                Text(text = "Avançar")
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun WelcomeScreenPreview() {
    ComprinhasTheme {
        WelcomeScreen {_, _ ->}
    }
}