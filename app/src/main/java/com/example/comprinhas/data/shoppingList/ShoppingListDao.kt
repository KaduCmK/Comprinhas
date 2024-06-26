package com.example.comprinhas.data.shoppingList

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {
    @Query("SELECT * FROM listaCompras;")
    fun getAllShoppingLists(): Flow<List<ShoppingList>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun createShoppingList(shoppingList: ShoppingList)

    @Update
    suspend fun updateShoppingList(shoppingList: ShoppingList)

    @Query("DELETE FROM listaCompras WHERE nomeLista = :listName AND senhaLista = :listPassword;")
    suspend fun deleteShoppingList(listName: String, listPassword: String)
}