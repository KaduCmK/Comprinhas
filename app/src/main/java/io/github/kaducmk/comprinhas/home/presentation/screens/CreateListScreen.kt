package io.github.kaducmk.comprinhas.home.presentation.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import io.github.kaducmk.comprinhas.core.data.model.Usuario
import io.github.kaducmk.comprinhas.home.data.model.HomeUiEvent
import io.github.kaducmk.comprinhas.home.data.model.HomeUiState
import io.github.kaducmk.comprinhas.home.data.model.ShoppingList
import io.github.kaducmk.comprinhas.ui.theme.ComprinhasTheme

@Composable
fun CreateListScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    uiEvent: (HomeUiEvent) -> Unit,
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Voltar"
                    )
                }
                Text(
                    text = "Nova lista",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome da lista") })
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha (opcional)") },
                visualTransformation = PasswordVisualTransformation()
            )
            Text("Imagem da lista em breve!", style = MaterialTheme.typography.titleMedium)

            Button({
                uiEvent(HomeUiEvent.OnCreateShoppingList(name, password))
                navController.popBackStack()
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Criar lista")
                Text("Criar nova lista")
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CreateListScreenPreview() {
    val creator = Usuario("1", "Kadu", "")
    ComprinhasTheme {
        CreateListScreen(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
            uiState = HomeUiState.Loaded(
                null,
                listOf(
                    ShoppingList(
                        "0", "Daiso", "", criador = creator, participantes = listOf(
                            creator, creator
                        )
                    ),
                    ShoppingList("1", "Centro", "", criador = creator, participantes = emptyList()),
                    ShoppingList(
                        "2",
                        "Mercado",
                        "",
                        criador = creator,
                        participantes = emptyList()
                    ),
                ),
                null
            ),
            uiEvent = {},
            navController = rememberNavController()
        )
    }
}