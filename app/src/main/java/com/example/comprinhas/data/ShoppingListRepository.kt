package com.example.comprinhas.data

import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {

    fun getAllShopping(): Flow<List<ShoppingItem>>

    fun getAllCart(): Flow<List<ShoppingItem>>

    suspend fun insert(item: ShoppingItem)

    suspend fun moveToCart(item: ShoppingItem)

    suspend fun deleteFromCart(item: ShoppingItem)

    suspend fun deleteFromList(item: ShoppingItem)

    suspend fun clearCart()
}