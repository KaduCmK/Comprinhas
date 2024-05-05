package com.example.comprinhas.ui.home

import android.app.Application
import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprinhas.ComprinhasViewModelPreview
import com.example.comprinhas.IMainViewModel
import com.example.comprinhas.ui.theme.ComprinhasTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: IMainViewModel,
    toWelcomeScreen: () -> Unit,
    toSettingsScreen: () -> Unit,
    showDialog: () -> Unit,
) {
    val cartList by viewModel.cartList.collectAsState(initial = emptyList())
    val shoppingList by viewModel.shoppingList.collectAsState(initial = emptyList())

    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    if (viewModel.appPreferences.welcomeScreen) toWelcomeScreen()

    BottomSheetScaffold(
        topBar = {
            Surface {
                TopBar(
                    showDialog = showDialog,
                    onQrCodeScan = viewModel::scanQrCode,
                    onNavigateToSettings = toSettingsScreen
                )
            }
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
                    viewModel.clearCart()
                    scope.launch { scaffoldState.bottomSheetState.partialExpand() }
                },
            )
        }
    ) {innerPadding ->
        ShoppingList(
            shoppingList = shoppingList,
            modifier = Modifier.padding(innerPadding),
            onMoveToCart = { viewModel.moveToCart(it) },
            onDelete = { viewModel.deleteShoppingItem(it) },
            isLoading = viewModel.isLoading
        )
    }
}

@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview(
) {
    ComprinhasTheme {
        HomeScreen(
            ComprinhasViewModelPreview(Application()),
            {}, {}, {}
            )
    }
}