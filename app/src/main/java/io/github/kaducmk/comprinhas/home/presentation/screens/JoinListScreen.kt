package io.github.kaducmk.comprinhas.home.presentation.screens

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import io.github.kaducmk.comprinhas.core.data.model.Usuario
import io.github.kaducmk.comprinhas.home.data.model.DialogState
import io.github.kaducmk.comprinhas.home.data.model.HomeUiEvent
import io.github.kaducmk.comprinhas.home.data.model.HomeUiState
import io.github.kaducmk.comprinhas.home.data.model.ShoppingList
import io.github.kaducmk.comprinhas.ui.theme.ComprinhasTheme

@Composable
fun JoinListScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    uiEvent: (HomeUiEvent) -> Unit,
    navController: NavController,
) {
    var name by remember { mutableStateOf("") }
    var listPassword by remember { mutableStateOf("") }

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
                    text = "Entrar em uma lista",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            ElevatedButton({
                uiEvent(HomeUiEvent.OnScanQrCode)
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = "Escanear QR Code"
                )
                Text("Escanear QR Code", style = MaterialTheme.typography.labelLarge)
            }
            Text("Ou", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text("entrar com nome e senha", style = MaterialTheme.typography.bodyMedium)
            TextField(
                name,
                onValueChange = {
                    name = it
                    if (it.length > 2) uiEvent(HomeUiEvent.OnSearchShoppingList(it))
                },
                label = { Text("Pesquisar lista") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Pesquisar por nome"
                    )
                })
            TextField(
                listPassword,
                onValueChange = { listPassword = it },
                label = { Text("Senha") },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(2.dp)
                    )
                    .fillMaxHeight()
                    .padding(4.dp)
                    .fillMaxWidth(0.9f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                (uiState as? HomeUiState.Loaded)?.dialogState?.let { state ->
                    when (state) {
                        is DialogState.Loading -> CircularProgressIndicator()
                        is DialogState.Loaded -> {
                            state.searchResult.forEach {
                                ElevatedCard(
                                    modifier = Modifier.padding(4.dp),
                                    onClick = {
                                        uiEvent(HomeUiEvent.OnJoinShoppingList(it.id, listPassword))
                                        navController.popBackStack()
                                    }
                                ) {
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        AsyncImage(
                                            model = it.criador.photoUrl,
                                            contentDescription = "Foto do criador",
                                        )
                                        Column {
                                            Text(
                                                text = it.nome,
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                            Text(
                                                text = it.criador.displayName!!,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun JoinListScreenPreview() {
    val creator = Usuario("1", "Kadu", "")
    ComprinhasTheme {
        JoinListScreen(
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