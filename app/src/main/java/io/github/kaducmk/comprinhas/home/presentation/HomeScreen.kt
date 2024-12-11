package io.github.kaducmk.comprinhas.home.presentation

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
import io.github.kaducmk.comprinhas.core.data.model.Usuario
import io.github.kaducmk.comprinhas.core.presentation.TopBar
import io.github.kaducmk.comprinhas.home.data.model.HomeUiEvent
import io.github.kaducmk.comprinhas.home.data.model.HomeUiState
import io.github.kaducmk.comprinhas.home.data.model.ShoppingList
import io.github.kaducmk.comprinhas.home.presentation.components.ShoppingListCard
import io.github.kaducmk.comprinhas.home.presentation.dialogs.NewListDialog
import io.github.kaducmk.comprinhas.ui.navigation.ToAuth
import io.github.kaducmk.comprinhas.ui.navigation.ToShoppingList
import io.github.kaducmk.comprinhas.ui.theme.ComprinhasTheme

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
            navController.navigate(ToAuth)
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
                        onClick = { uiEvent(HomeUiEvent.OnDialog(true)) },
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
                        onClick = { uiEvent(HomeUiEvent.OnDialog(false)) },
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
        when (uiState) {
            is HomeUiState.Loading -> {}

            is HomeUiState.NoInternet -> {
                Icon(
                    imageVector = Icons.Outlined.CloudOff,
                    contentDescription = "Sem Internet"
                )
            }

            is HomeUiState.Error -> {
                Text(text = "Erro: ${uiState.message}")
            }

            is HomeUiState.Loaded -> {
                uiState.dialogState?.let {
                    NewListDialog(
                        onDismiss = { uiEvent(HomeUiEvent.OnDialog(null)) },
                        dialogState = uiState.dialogState,
                        onSearch = { uiEvent(HomeUiEvent.OnSearchShoppingList(it)) },
                        onJoinList = { nameOrId, password ->
                            if (it.newList) uiEvent(
                                HomeUiEvent.OnCreateShoppingList(
                                    nameOrId,
                                    password
                                )
                            )
                            else uiEvent(HomeUiEvent.OnJoinShoppingList(nameOrId, password))
                        })
                }

                LazyVerticalGrid(
                    modifier = Modifier.padding(paddingValues),
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(uiState.lists, key = { it.id }) {
                        ShoppingListCard(
                            modifier = Modifier.animateItem(),
                            shoppingList = it,
                            onCardClick = {
                                navController.navigate(ToShoppingList(it.id))
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