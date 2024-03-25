package com.example.comprinhas

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comprinhas.data.ShoppingItem
import com.example.comprinhas.ui.theme.ComprinhasTheme

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
}

@Composable
fun MainApp(comprinhasViewModel: ComprinhasViewModel = viewModel()) {
    val userPreferences = comprinhasViewModel.appPreferences

    if (userPreferences.welcomeScreen) {
        WelcomeScreen {
            comprinhasViewModel.updateUserPrefs(it)
        }
    }
    else {
        HomeScreen(name = userPreferences.name)
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    comprinhasViewModel: ComprinhasViewModel = viewModel(),
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
        ShoppingList(
            shoppingFlow = comprinhasViewModel.shoppingList,
            modifier = Modifier.padding(innerPadding),
            isExpanded = isExpanded,
            onMoveToCart = { comprinhasViewModel.moveToCart(it)},
            onDelete = { comprinhasViewModel.deleteShoppingItem(it)}
        )
    }
}

@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    ComprinhasTheme {
        HomeScreen(name = "Fulano")
    }
}