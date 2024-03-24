package com.example.comprinhas

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comprinhas.data.AppPreferences
import com.example.comprinhas.data.PreferencesRepository
import com.example.comprinhas.data.ShoppingItem
import com.example.comprinhas.data.ShoppingListDatabase
import kotlinx.coroutines.launch

//fun mockShoppingList() = List(7) { ShoppingItem("Compra $it", "Mock")}

class ComprinhasViewModel(application: Application): AndroidViewModel(application) {
    private val db = ShoppingListDatabase.getDatabase(application.applicationContext)
    private val dao = db.shoppingItemDao()

    private val preferencesRepository =
        PreferencesRepository(application.dataStore, application.applicationContext)
    private val preferencesFlow =
        preferencesRepository.preferencesFlow

    private var _appPreferences by mutableStateOf(AppPreferences("", false))
    private var _shoppingList = dao.getAllShopping().toMutableStateList()
    private var _cartList = dao.getAllCart().toMutableStateList()

    val appPreferences: AppPreferences
        get() {
            viewModelScope.launch {
                preferencesFlow.collect {
                    _appPreferences = AppPreferences(it.name, it.welcomeScreen)
                }
            }

            return _appPreferences
        }

    val shoppingList: List<ShoppingItem>
        get() = _shoppingList

    val cartList: List<ShoppingItem>
        get() = _cartList

    fun addShoppingList(item: ShoppingItem) {
        dao.insert(item)
        _shoppingList.add(item)
    }

    fun deleteShoppingItem(item: ShoppingItem) {
        dao.deleteFromList(item)
        _shoppingList.remove(item)
    }

    fun moveToCart(item: ShoppingItem) {
        dao.moveToCart(item.id)
        _shoppingList.remove(item)
        _cartList.add(item)

    }

    fun removeFromCart(item: ShoppingItem) {
        dao.removeFromCart(item.id)
        _cartList.remove(item)
        _shoppingList.add(item)
    }
    fun clearCart() {
        dao.clearCart()
        _cartList.clear()
    }

    fun updateUserPrefs(name: String) {
        viewModelScope.launch {
            preferencesRepository.updateNameAndWelcomeScreen(name, false)
        }
    }
}