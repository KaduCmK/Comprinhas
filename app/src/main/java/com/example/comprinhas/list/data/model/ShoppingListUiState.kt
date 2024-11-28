package com.example.comprinhas.list.data.model

import android.graphics.Bitmap
import com.example.comprinhas.home.data.model.ShoppingList

sealed class ShoppingListUiState {
    abstract val shoppingList: ShoppingList?
    abstract val shoppingItems: List<ShoppingItem>

    data class Loading(
        override val shoppingList: ShoppingList?,
        override val shoppingItems: List<ShoppingItem>
    ) : ShoppingListUiState()

    data class Loaded(
        override val shoppingList: ShoppingList,
        override val shoppingItems: List<ShoppingItem>,
        val dialogState: Pair<Boolean, ShoppingItem?>,
        val qrCode: Bitmap? = null
    ) : ShoppingListUiState()
}