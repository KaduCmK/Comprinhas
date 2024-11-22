package com.example.comprinhas.list.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.comprinhas.ComprinhasViewModel
import com.example.comprinhas.ui.TopBar
import com.example.comprinhas.ui.UiState
import com.example.comprinhas.list.presentation.components.BottomBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    viewModel: ComprinhasViewModel,
    uiState: UiState,
    toWelcomeScreen: () -> Unit,
    listId: Int,
    toSettingsScreen: () -> Unit,
    toReceiptsScreen: () -> Unit,
    showDialog: () -> Unit,
) {
    val cartList by viewModel.cartList.collectAsState(initial = emptyList())
    val shoppingList by viewModel.shoppingList.collectAsState(initial = emptyList())

    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = 1) {
//        if (viewModel.welcomeScreen) toWelcomeScreen()
//        else {
            viewModel.getCurrentList(listId)
//            viewModel.getShoppingList()
//        }
    }

    BottomSheetScaffold(
        topBar = {
            TopBar(
                title = "Lista  de Compras",
                subtitle = viewModel.currentList.nomeLista,
                mainButton = {
                    Button(
                        enabled = it == UiState.LOADED,
                        onClick = showDialog
                    ) {
                        Text(

                            style = MaterialTheme.typography.titleMedium,
                            text = "Adicionar"
                        )
                    }
                },
                topButton = {
                    IconButton(onClick = toSettingsScreen) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Configurações"
                        )
                    }
                },
                bottomButton = {
                    IconButton(onClick = toReceiptsScreen,)  {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ReceiptLong,
                            contentDescription = "Notas Fiscais"
                        )
                    }
                },
                uiState = uiState
            )
     },
        sheetPeekHeight = 115.dp,
        scaffoldState = scaffoldState,
        sheetContent = {
            BottomBar(
                cartList = cartList,
                onRemoveItem = {
                    viewModel.removeFromCart(it)
                },
                onClearCart = {
                    viewModel.clearCart(listId)
                    scope.launch { scaffoldState.bottomSheetState.partialExpand() }
                },
            )
        }
    ) {innerPadding ->
        if (uiState == UiState.LOADING) {
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
        else {
            ShoppingList(
                shoppingList = shoppingList.filter { it.idLista == listId },
                modifier = Modifier.padding(innerPadding),
                onMoveToCart = { viewModel.moveToCart(it) },
                onDelete = { viewModel.deleteShoppingItem(it) },
            )
        }
    }
}

//@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Preview(showBackground = true)
//@Composable
//private fun HomeScreenPreview(
//) {
//    ComprinhasTheme {
//        ShoppingListScreen(
//            ComprinhasViewModel(Application()), uiState = UiState.LOADED,
//            {}, {}, {}, {}
//            )
//    }
//}