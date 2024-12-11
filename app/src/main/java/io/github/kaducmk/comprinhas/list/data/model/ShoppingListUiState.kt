package io.github.kaducmk.comprinhas.list.data.model

import android.graphics.Bitmap
import io.github.kaducmk.comprinhas.core.data.model.Usuario
import io.github.kaducmk.comprinhas.home.data.model.ShoppingList

sealed class ShoppingListUiState {
    abstract val currentUser: Usuario?
    abstract val shoppingList: ShoppingList?
    abstract val shoppingItems: List<ShoppingItem>
    abstract val carrinho: List<CartItem>

    data class Loading(
        override val currentUser: Usuario?,
        override val shoppingList: ShoppingList?,
        override val shoppingItems: List<ShoppingItem>,
        override val carrinho: List<CartItem>
    ) : ShoppingListUiState()

    data class Loaded(
        override val currentUser: Usuario?,
        override val shoppingList: ShoppingList,
        override val shoppingItems: List<ShoppingItem>,
        override val carrinho: List<CartItem>,
        val dialogState: Pair<Boolean, ShoppingItem?>,
        val qrCode: Bitmap? = null
    ) : ShoppingListUiState()
}