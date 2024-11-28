package com.example.comprinhas

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.comprinhas.list.data.model.ShoppingItem
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

@Deprecated("Passando para viewModels especificas de cada tela")
class ComprinhasViewModel(private val application: Application): AndroidViewModel(application) {
    //    fun exitShoppingList(listName: String, listPassword: String) {
//        viewModelScope.launch {
//            repo.exitShoppingList(listName, listPassword)
//        }
//    }
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