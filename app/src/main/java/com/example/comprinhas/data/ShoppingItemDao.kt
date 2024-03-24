package com.example.comprinhas.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.time.LocalDateTime
import java.time.ZonedDateTime

@Dao
interface ShoppingItemDao {

    @Query("SELECT * from items WHERE onCart = 0")
    fun getAllShopping(): List<ShoppingItem>

    @Query("SELECT * FROM items WHERE onCart = 1")
    fun getAllCart(): List<ShoppingItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: ShoppingItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<ShoppingItem>)

    @Query("UPDATE items SET onCart = 1 WHERE id = :id")
    fun moveToCart(id: ZonedDateTime)

    @Query("UPDATE items SET onCart = 0 WHERE id = :id")
    fun removeFromCart(id: ZonedDateTime)

    @Delete
    fun deleteFromList(item: ShoppingItem)

    @Query("DELETE FROM items WHERE onCart = 1")
    fun clearCart()
}