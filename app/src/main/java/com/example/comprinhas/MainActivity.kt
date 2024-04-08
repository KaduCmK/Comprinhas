package com.example.comprinhas

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comprinhas.data.ShoppingItem
import com.example.comprinhas.ui.theme.ComprinhasTheme

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

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    comprinhasViewModel: IMainViewModel = viewModel(),
    name: String
) {
    var isExpanded by remember { mutableStateOf(false) }
    var dialog by remember { mutableStateOf(false) }

    if (dialog) {
        InputDialog(
            setDialog = { dialog = it },
            setValue = {
                comprinhasViewModel.addShoppingList(ShoppingItem(name = it, addedBy = name))
            })
    }
    
    Scaffold(
        topBar = {
            TopBar(isExpanded = isExpanded, showDialog = { dialog = true })
        },
        bottomBar = {
            BottomBar(
                cartFlow = comprinhasViewModel.cartList,
                isExpanded = isExpanded,
                toggleExpanded = { isExpanded = !isExpanded },
                onRemoveItem = { comprinhasViewModel.removeFromCart(it) },
                onClearCart = {
                    comprinhasViewModel.clearCart()
                    isExpanded = false
                }
            )
        }
    ) {innerPadding ->
        if (comprinhasViewModel.isLoading) {
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
                shoppingFlow = comprinhasViewModel.shoppingList,
                modifier = Modifier.padding(innerPadding),
                isExpanded = isExpanded,
                onMoveToCart = { comprinhasViewModel.moveToCart(it)},
                onDelete = { comprinhasViewModel.deleteShoppingItem(it)}
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
        HomeScreen(
            comprinhasViewModel = ComprinhasViewModelPreview(Application()),
            name = "Preview")
    }
}