package com.example.comprinhas.list.data.model

sealed interface ShoppingListUiEvent {
    data class OnSetShoppingList(val uid: String) : ShoppingListUiEvent
    data class OnAddShoppingItem(val listUid: String, val nome: String) : ShoppingListUiEvent
}