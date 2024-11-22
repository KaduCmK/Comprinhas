package com.example.comprinhas.home.presentation

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.outlined.LibraryAdd
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprinhas.home.data.ShoppingList
import com.example.comprinhas.ui.TopBar
import com.example.comprinhas.ui.UiState
import com.example.comprinhas.home.presentation.components.ShoppingListCard
import com.example.comprinhas.ui.theme.ComprinhasTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    shoppingLists: List<ShoppingList>,
    welcome: Boolean,
    getShoppingItems: () -> Unit,
    toWelcomeScreen: () -> Unit,
    onJoinList: (Boolean) -> Unit,
    onNavigateToList: (listId: Int) -> Unit,
    onHoldList: (ShoppingList) -> Unit,
    uiState: UiState
) {
    LaunchedEffect(key1 = 1) {
        if (welcome) toWelcomeScreen()
        else getShoppingItems()
    }

    Scaffold(
        contentWindowInsets = WindowInsets(16, 8, 16, 4),
        topBar = {
            TopBar(
                title = "Comprinhas",
                mainButton = {
                    Button(
                        onClick = { onJoinList(true) },
                        enabled = uiState == UiState.LOADED
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
                        onClick = { onJoinList(false) },
                        enabled = uiState == UiState.LOADED
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
        Surface (
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        )  {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(shoppingLists, key = { it.idLista }) {
                    ShoppingListCard(
                        modifier = Modifier.animateItemPlacement(),
                        shoppingList = it,
                        onCardClick = { onNavigateToList(it.idLista) },
                        onCardHold = { onHoldList(it) }
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenPreview() {
    ComprinhasTheme {
        HomeScreen(
            onJoinList = {},
            shoppingLists = listOf(
                ShoppingList(0, "Daiso", "", "Kadu"),
                ShoppingList(1, "Centro", "", "Kadu"),
                ShoppingList(2, "Mercado", "", "Kadu"),
            ),
            welcome = false,
            toWelcomeScreen = {},
            getShoppingItems = {},
            onNavigateToList = {},
            onHoldList = {},
            uiState = UiState.LOADED
        )
    }
}