package com.example.comprinhas

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

//fun mockShoppingList() = List(7) { ShoppingItem("Compra $it", "Mock")}

class ComprinhasViewModel(): ViewModel() {

    private var _shoppingList = mutableStateListOf<ShoppingItem>()
    private var _cartList = mutableStateListOf<ShoppingItem>()

    val shoppingList: List<ShoppingItem>
        get() = _shoppingList
    val cartList: List<ShoppingItem>
        get() = _cartList

    fun addShoppingList(item: ShoppingItem) {
        _shoppingList.add(item)
    }

    fun moveToCart(item: ShoppingItem) {
        _shoppingList.remove(item)
        _cartList.add(item)
    }

    fun removeFromCart(item: ShoppingItem) {
        _cartList.remove(item)
    }
    fun clearCart() {
        _cartList.clear()
    }
}