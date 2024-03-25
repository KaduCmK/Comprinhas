package com.example.comprinhas

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.comprinhas.http.DatabaseApi
import androidx.lifecycle.viewModelScope
import com.example.comprinhas.data.AppPreferences
import com.example.comprinhas.data.PreferencesRepository
import com.example.comprinhas.data.ShoppingItem
import com.example.comprinhas.data.ShoppingListDatabase
import com.example.comprinhas.data.ShoppingListRepository
import kotlinx.coroutines.launch

//fun mockShoppingList() = List(7) { ShoppingItem("Compra $it", "Mock")}

class ComprinhasViewModel(application: Application): AndroidViewModel(application) {
    private val db = ShoppingListDatabase.getDatabase(application.applicationContext)
    private val dao = db.shoppingItemDao()
    private val repo = ShoppingListRepository(dao)

    private val preferencesRepository =
        PreferencesRepository(application.dataStore, application.applicationContext)
    private val preferencesFlow =
        preferencesRepository.preferencesFlow

    private var _appPreferences by mutableStateOf(AppPreferences("", false))
    var shoppingList = repo.shoppingList
    var cartList = repo.cartList

    init {
        viewModelScope.launch {
            val result = DatabaseApi.retrofitService.getDatabase()
            repo.clearList()
            result.shoppingList.forEach {
                repo.insert(it)
            }
        }
    }

    val appPreferences: AppPreferences
        get() {
            viewModelScope.launch {
                preferencesFlow.collect {
                    _appPreferences = AppPreferences(it.name, it.welcomeScreen)
                }
            }

            return _appPreferences
        }


    fun addShoppingList(item: ShoppingItem) {
        viewModelScope.launch {
            DatabaseApi.retrofitService.addNewItem(item.id, item.name, item.addedBy)
            repo.insert(item)
        }
//        shoppingList.add(item)
    }

    fun deleteShoppingItem(item: ShoppingItem) {
        viewModelScope.launch{
            DatabaseApi.retrofitService.removeItem(item.id)
            repo.deleteFromList(item)
        }
//        shoppingList.remove(item)
    }

    fun moveToCart(item: ShoppingItem) {
        viewModelScope.launch {
            repo.moveToCart(item.id)
        }
//        shoppingList.remove(item)
//        cartList.add(item)

    }

    fun removeFromCart(item: ShoppingItem) {
        viewModelScope.launch { repo.removeFromCart(item.id) }
//        cartList.remove(item)
//        shoppingList.add(item)
    }
    fun clearCart() {
        viewModelScope.launch {
            cartList.collect { shoppingItems ->
                DatabaseApi.retrofitService.clearCart(shoppingItems.map { it.id })
            }
            repo.clearCart()
        }
    }

    fun updateUserPrefs(name: String) {
        viewModelScope.launch {
            preferencesRepository.updateNameAndWelcomeScreen(name, false)
        }
    }
}