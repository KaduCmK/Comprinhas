package com.example.comprinhas

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.comprinhas.core.data.AppPreferences
import com.example.comprinhas.core.data.PreferencesRepository
import com.example.comprinhas.list.data.model.ShoppingItem
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class ComprinhasViewModel(private val application: Application): AndroidViewModel(application) {
    private val preferencesRepository =
        PreferencesRepository(application.dataStore)

    var uiState = preferencesRepository.uiState

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