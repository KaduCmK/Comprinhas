package com.example.comprinhas

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comprinhas.data.ShoppingItem
import com.example.comprinhas.ui.theme.ComprinhasTheme
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    companion object { var isForeground = false }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isForeground = true

        val name = "Adição e remoção de itens"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("list_notifications", name, importance)

        val notificationManager: NotificationManager =
            application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        setContent {
            ComprinhasTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        isForeground = false
    }
}

@Composable
fun MainApp(comprinhasViewModel: ComprinhasViewModel = viewModel()) {
    val userPreferences = comprinhasViewModel.appPreferences

    if (userPreferences.welcomeScreen) {
        WelcomeScreen { name, listId ->
            comprinhasViewModel.updateUserPrefs(name, listId)
        }
    }
    else {
        HomeScreen(name = userPreferences.name, comprinhasViewModel = comprinhasViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    comprinhasViewModel: IMainViewModel = viewModel(),
    name: String
) {
    val cartList by comprinhasViewModel.cartList.collectAsState(initial = emptyList())
    val shoppingList by comprinhasViewModel.shoppingList.collectAsState(initial = emptyList())

    var dialog by remember { mutableStateOf(false) }

    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    if (dialog) {
        InputDialog(
            setDialog = { dialog = it },
            setValue = {
                comprinhasViewModel.addShoppingList(ShoppingItem(name = it, addedBy = name))
            })
    }

    BottomSheetScaffold(
        topBar = {
            Surface {
                TopBar(showDialog = { dialog = true })
            }
        },
        sheetPeekHeight = 115.dp,
        scaffoldState = scaffoldState,
        sheetContent = {
            BottomBar(
                cartList = cartList,
                onRemoveItem = {
                    comprinhasViewModel.removeFromCart(it)
               },
                onClearCart = {
                    comprinhasViewModel.clearCart()
                    scope.launch { scaffoldState.bottomSheetState.partialExpand() }
                },
            )
        }
    ) {innerPadding ->
        ShoppingList(
            shoppingList = shoppingList,
            modifier = Modifier.padding(innerPadding),
            onMoveToCart = { comprinhasViewModel.moveToCart(it)},
            onDelete = { comprinhasViewModel.deleteShoppingItem(it)},
            isLoading = comprinhasViewModel.isLoading
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
            comprinhasViewModel = ComprinhasViewModelPreview(Application()),
            name = "Preview")
    }
}