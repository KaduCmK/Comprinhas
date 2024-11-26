package com.example.comprinhas.list.data.model

import com.example.comprinhas.home.data.model.ShoppingList

sealed interface ShoppingListUiEvent {
    data class OnSetShoppingList(val uid: String) : ShoppingListUiEvent
}