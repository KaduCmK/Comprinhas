package com.example.comprinhas.home.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.LibraryAdd
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.comprinhas.core.data.model.Usuario
import com.example.comprinhas.core.presentation.TopBar
import com.example.comprinhas.home.data.model.HomeUiEvent
import com.example.comprinhas.home.data.model.HomeUiState
import com.example.comprinhas.home.data.model.ShoppingList
import com.example.comprinhas.home.presentation.components.ShoppingListCard
import com.example.comprinhas.ui.navigation.Auth
import com.example.comprinhas.ui.theme.ComprinhasTheme

@Composable
fun HomeScreenRoot(
    modifier: Modifier = Modifier,
    navController: NavController,
    currentUser: Usuario?
) {
    val viewModel = hiltViewModel<HomeViewModel>()
    HomeScreen(
        modifier = modifier,
        currentUser = currentUser,
        uiState = viewModel.uiState.collectAsState().value,
        uiEvent = viewModel::onEvent,
        navController = navController,
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    currentUser: Usuario?,
    uiState: HomeUiState,
    uiEvent: (HomeUiEvent) -> Unit,
    navController: NavController
) {
    LaunchedEffect(key1 = 1) {
        if (currentUser == null)
            navController.navigate(Auth)
        else uiEvent(HomeUiEvent.OnGetShoppingLists(currentUser))
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(16, 8, 16, 4),
        topBar = {
            TopBar(
                title = "Comprinhas",
                mainButton = {
                    Button(
                        onClick = { uiEvent(HomeUiEvent.OnCreateShoppingList("Teste", "123")) },
                        enabled = uiState is HomeUiState.Loaded
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LibraryAdd,
                            contentDescription = "Criar Lista"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Criar Lista",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                bottomButton = {
                    TextButton(
                        onClick = { },
                        enabled = uiState is HomeUiState.Loaded
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Login,
                            contentDescription = "Entrar em Lista"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Entrar em Lista",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                uiState = uiState
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            modifier = Modifier.padding(paddingValues),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            when (uiState) {
                is HomeUiState.Loading -> {}

                is HomeUiState.NoInternet -> {
                    item {
                        Icon(
                            imageVector = Icons.Outlined.CloudOff,
                            contentDescription = "Sem Internet"
                        )
                    }
                }

                is HomeUiState.Error -> {
                    item {
                        Text(text = "Erro: ${uiState.message}")
                    }
                }

                is HomeUiState.Loaded -> {
                    items(uiState.lists, key = { it.id }) {
                        ShoppingListCard(
                            modifier = Modifier.animateItem(),
                            shoppingList = it,
                            onCardClick = {
//                                navController.navigate(
//                                    com.example.comprinhas.ui.navigation.ShoppingList(
//                                        it.id
//                                    )
//                                )
                            },
                            onCardHold = { list -> uiEvent(HomeUiEvent.OnHoldCard(list)) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenPreview() {
    val creator = Usuario("1", "Kadu", "")
    ComprinhasTheme {
        HomeScreen(
            currentUser = null,
            uiState = HomeUiState.Loaded(
                null, listOf(
                    ShoppingList("0", "Daiso", "", criador = creator, participantes = emptyList()),
                    ShoppingList("1", "Centro", "", criador = creator, participantes = emptyList()),
                    ShoppingList("2", "Mercado", "", criador = creator, participantes = emptyList()),
                )
            ),
            uiEvent = {},
            navController = rememberNavController()
        )
    }
}