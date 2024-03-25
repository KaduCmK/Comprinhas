package com.example.comprinhas.data

import kotlinx.coroutines.flow.Flow

class ShoppingListRepository(private val dao: ShoppingItemDao) {

    val shoppingList: Flow<List<ShoppingItem>> = dao.getAllShopping()
    val cartList: Flow<List<ShoppingItem>> = dao.getAllCart()

    suspend fun insert(item: ShoppingItem) {
        dao.insert(item)
    }

    suspend fun deleteFromList(item: ShoppingItem) {
        dao.deleteFromList(item)
    }

    suspend fun moveToCart(id: Long) {
        dao.moveToCart(id)
    }

    suspend fun removeFromCart(id: Long) {
        dao.removeFromCart(id)
    }

    suspend fun clearCart() {
        dao.clearCart()
    }

}