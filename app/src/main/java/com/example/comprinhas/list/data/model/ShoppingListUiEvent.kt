package com.example.comprinhas.list.data.model

sealed interface ShoppingListUiEvent {
    data class OnSetShoppingList(val uid: String) : ShoppingListUiEvent
    data class OnAddShoppingItem(val nome: String) : ShoppingListUiEvent
    data class OnToggleDialog(val dialog: Boolean) : ShoppingListUiEvent
}