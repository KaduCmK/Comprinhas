package com.example.comprinhas.list.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
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
import com.example.comprinhas.list.data.model.ShoppingListUiEvent
import com.example.comprinhas.list.data.model.ShoppingListUiState
import com.example.comprinhas.list.presentation.components.bottom_sheet.BottomBar
import com.example.comprinhas.list.presentation.components.ListTopBar
import com.example.comprinhas.list.presentation.components.ShoppingList
import com.example.comprinhas.list.presentation.dialogs.NewItemDialog
import com.example.comprinhas.ui.theme.ComprinhasTheme

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
        uiEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    uiState: ShoppingListUiState,
    uiEvent: (ShoppingListUiEvent) -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        modifier = modifier,
        topBar = {
            ListTopBar(
                uiState = uiState,
                onAddItem = { uiEvent(ShoppingListUiEvent.OnToggleDialog(true)) },
                onShowQrCode = { uiEvent(ShoppingListUiEvent.OnToggleQrCode(it)) }
            )
        },
        sheetPeekHeight = 115.dp,
        scaffoldState = scaffoldState,
        sheetContent = {
            BottomBar(
                uiState = uiState,
                scaffoldState = scaffoldState,
                uiEvent = uiEvent
            )
        }
    ) { innerPadding ->
        ShoppingList(
            modifier = Modifier.padding(innerPadding),
            shoppingList = uiState.shoppingItems,
            uiEvent = uiEvent
        )
        if ((uiState as? ShoppingListUiState.Loaded)?.dialogState?.first == true) {
            NewItemDialog(
                editItem = uiState.dialogState.second,
                onDismiss = { uiEvent(ShoppingListUiEvent.OnToggleDialog(false)) },
                onConfirm = { name, uid ->
                    if (uid == null) {
                        uiEvent(ShoppingListUiEvent.OnAddShoppingItem(name))
                    } else {
                        uiEvent(ShoppingListUiEvent.OnEditShoppingItem(uid, name))
                    }
                }
            )
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
                currentUser = null,
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
                    participantes = emptyList(),
                    carrinho = emptyList()
                ),
                emptyList(),
                emptyList()
            ),
            uiEvent = {}
        )
    }
}