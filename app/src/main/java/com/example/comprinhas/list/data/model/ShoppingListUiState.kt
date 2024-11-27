package com.example.comprinhas.list.data.model

import com.example.comprinhas.home.data.model.DialogState
import com.example.comprinhas.home.data.model.ShoppingList

sealed class ShoppingListUiState {
    abstract val shoppingList: ShoppingList?

    data class Loading(override val shoppingList: ShoppingList?) : ShoppingListUiState()
    data class Loaded(
        override val shoppingList: ShoppingList,
        val shoppingItems: List<ShoppingItem>,
        val dialogState: Boolean
    ) : ShoppingListUiState()
}