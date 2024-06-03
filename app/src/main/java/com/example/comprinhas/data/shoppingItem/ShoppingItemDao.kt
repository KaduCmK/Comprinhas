package com.example.comprinhas.data.shoppingItem

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingItemDao {

    @Query("SELECT * from items WHERE onCart = 0")
    fun getAllShopping(): Flow<List<ShoppingItem>>

    @Query("SELECT * FROM items WHERE onCart = 1")
    fun getAllCart(): Flow<List<ShoppingItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ShoppingItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<ShoppingItem>)

    @Query("UPDATE items SET onCart = 1 WHERE idItem = :id")
    suspend fun moveToCart(id: Long)

    @Query("UPDATE items SET onCart = 0 WHERE idItem = :id")
    suspend fun removeFromCart(id: Long)

    @Delete
    suspend fun deleteFromList(item: ShoppingItem)

    @Query("DELETE FROM items WHERE onCart = 1")
    suspend fun clearCart()

    @Query("DELETE FROM items")
    suspend fun clearList()
}