package com.example.comprinhas.list.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.comprinhas.core.data.model.Usuario
import com.example.comprinhas.list.data.model.ShoppingListUiEvent
import com.example.comprinhas.list.data.model.ShoppingListUiState
import com.example.comprinhas.list.presentation.components.BottomBar
import com.example.comprinhas.list.presentation.components.ListTopBar
import com.example.comprinhas.list.presentation.components.ShoppingList
import com.example.comprinhas.list.presentation.dialogs.NewItemDialog
import com.example.comprinhas.ui.theme.ComprinhasTheme
import kotlinx.coroutines.launch

@Composable
fun ShoppingListScreenRoot(
    modifier: Modifier = Modifier,
    navController: NavController,
    listId: String
) {
    val viewModel = hiltViewModel<ShoppingListViewModel>()

    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(ShoppingListUiEvent.OnSetShoppingList(listId))
    }

    ShoppingListScreen(
        modifier = modifier,
        navController = navController,
        uiState = viewModel.uiState.collectAsState().value,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    uiState: ShoppingListUiState,
    onEvent: (ShoppingListUiEvent) -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        modifier = modifier,
        topBar = {
            ListTopBar(
                uiState = uiState,
                onAddItem = { onEvent(ShoppingListUiEvent.OnToggleDialog(true)) }
            )
        },
        sheetPeekHeight = 115.dp,
        scaffoldState = scaffoldState,
        sheetContent = {
            BottomBar(
                cartList = emptyList(),
                onRemoveItem = {},
                onClearCart = {
//                    viewModel.clearCart(listId)
                    scope.launch { scaffoldState.bottomSheetState.partialExpand() }
                },
            )
        }
    ) { innerPadding ->
        when (uiState) {
            is ShoppingListUiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(32.dp)
                    )
                }
            }

            is ShoppingListUiState.Loaded -> {
                if (uiState.dialogState.first) {
                    NewItemDialog(
                        editItem = uiState.dialogState.second,
                        onDismiss = { onEvent(ShoppingListUiEvent.OnToggleDialog(false)) },
                        onConfirm = { name, uid ->
                            if (uid == null) {
                                onEvent(ShoppingListUiEvent.OnAddShoppingItem(name))
                            } else {
                                onEvent(ShoppingListUiEvent.OnEditShoppingItem(uid, name))
                            }
                        }
                    )
                }

                ShoppingList(
                    modifier = Modifier.padding(innerPadding),
                    shoppingList = uiState.shoppingItems,
                    onMoveToCart = {},
                    onDelete = { onEvent(ShoppingListUiEvent.OnDeleteShoppingItem(it.id)) },
                    onEdit = { onEvent(ShoppingListUiEvent.OnToggleDialog(true, it)) }
                )
            }
        }
    }
}

@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview(
) {
    ComprinhasTheme {
        ShoppingListScreen(
            navController = rememberNavController(),
            uiState = ShoppingListUiState.Loading(
                com.example.comprinhas.home.data.model.ShoppingList(
                    id = "1",
                    nome = "Daiso",
                    senha = "",
                    criador = Usuario(
                        uid = "",
                        displayName = "Lucas",
                        email = "",
                        photoUrl = ""
                    ),
                    participantes = emptyList()
                )
            ),
            onEvent = {}
        )
    }
}