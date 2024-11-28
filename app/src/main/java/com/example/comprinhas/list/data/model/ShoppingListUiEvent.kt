package com.example.comprinhas.list.data.model

sealed interface ShoppingListUiEvent {
    data class OnSetShoppingList(val uid: String) : ShoppingListUiEvent
    data class OnAddShoppingItem(val nome: String) : ShoppingListUiEvent
    data class OnDeleteShoppingItem(val uid: String) : ShoppingListUiEvent
    data class OnEditShoppingItem(val uid: String, val nome: String) : ShoppingListUiEvent
    data class OnToggleDialog(val dialog: Boolean, val editItem: ShoppingItem? = null) : ShoppingListUiEvent
    data class OnToggleQrCode(val toggle: Boolean) : ShoppingListUiEvent
}