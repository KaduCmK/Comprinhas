package com.example.comprinhas

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.comprinhas.core.data.AppPreferences
import com.example.comprinhas.core.data.PreferencesRepository
import com.example.comprinhas.list.data.model.ShoppingItem
import com.example.comprinhas.home.data.model.ShoppingList
import com.example.comprinhas.ui.UiState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class ComprinhasViewModel(private val application: Application): AndroidViewModel(application) {
//    private val db = ComprinhasDatabase.getDatabase(application.applicationContext)
//    private val repo = ShoppingListRepository(
//        db.shoppingItemDao(),
//        db.shoppingListDao(),
//        application.baseContext
//    )

    private val preferencesRepository =
        PreferencesRepository(application.dataStore)
    private val preferencesFlow =
        preferencesRepository.preferencesFlow

    private var _appPreferences by mutableStateOf(
        AppPreferences(false, "", 0)
    )
    val appPreferences: AppPreferences
        get() {
            viewModelScope.launch {
                preferencesFlow.collect {
                    _appPreferences = AppPreferences(it.welcomeScreen, it.name, it.lastChanged)
                }
            }

            return _appPreferences
        }
//
//    var shoppingLists = repo.lists
//    var shoppingList = repo.list
//    var cartList = repo.cartList

    var uiState = preferencesRepository.uiState

    fun createShopplingList(listName: String, listPassword: String) {
        viewModelScope.launch {
            preferencesRepository.updateUiState(UiState.LOADING)

//            val res = repo.createShoppingList(appPreferences.name, listName, listPassword)
//            Toast.makeText(application.baseContext, res, Toast.LENGTH_SHORT).show()

            preferencesRepository.updateUiState(UiState.LOADED)
        }
    }

//    fun joinShoppingList(listName: String, listPassword: String) {
//        viewModelScope.launch {
//            preferencesRepository.updateUiState(UiState.LOADING)
//
//            val res = repo.joinShoppingList(appPreferences.name, listName, listPassword)
//            if (res.isNullOrEmpty()) {
//                getShoppingList()
//            }
//            else {
//                Toast.makeText(application.baseContext, res, Toast.LENGTH_SHORT).show()
//            }
//
//            preferencesRepository.updateUiState(UiState.LOADED)
//        }
//    }
//
//    fun deleteList(listName: String, listPassword: String) {
//        viewModelScope.launch {
//            repo.deleteList(appPreferences.name, listName, listPassword)
//        }
//    }
//
//    fun exitShoppingList(listName: String, listPassword: String) {
//        viewModelScope.launch {
//            repo.exitShoppingList(listName, listPassword)
//        }
//    }

//    var currentList by mutableStateOf(ShoppingList("sadasdasdad", "", "", ""))
//    fun getCurrentList(listId: String) {
//        viewModelScope.launch {
//            repo.lists.collect { list ->
//                currentList = list.find { it.id == listId } ?: ShoppingList("asdad", "", "", "")
//                Log.d("VIEW-MODEL", "Mudando para lista ${currentList.id}/${currentList.nome}")
//            }
//        }
//
//    }

    fun addShoppingListItem(item: ShoppingItem) {
        viewModelScope.launch {
//            preferencesRepository.updateLastChanged(item.idItem)
//            repo.insert(item)
        }
    }

    fun deleteShoppingItem(item: ShoppingItem) {
        viewModelScope.launch{
            val lastChanged = ZonedDateTime.now().toEpochSecond()
//            preferencesRepository.updateLastChanged(lastChanged)
//            repo.deleteFromList(item, lastChanged)
        }
    }

    fun moveToCart(item: ShoppingItem) {
        viewModelScope.launch {
//            repo.moveToCart(item.idItem)
        }

    }

    fun removeFromCart(item: ShoppingItem) {
//        viewModelScope.launch { repo.removeFromCart(item.idItem) }
    }
    fun clearCart(listId: Int) {
        viewModelScope.launch {
            val lastChanged = ZonedDateTime.now().toEpochSecond()

//            preferencesRepository.updateLastChanged(lastChanged)
//            repo.clearCart(cartList.first().map { it.idItem }, listId, lastChanged)
        }
    }
}