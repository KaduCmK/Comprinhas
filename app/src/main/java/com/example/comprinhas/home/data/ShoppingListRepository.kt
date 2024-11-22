package com.example.comprinhas.home.data

import android.content.Context
import android.util.Log
import com.example.comprinhas.data.shoppingItem.ShoppingItem
import com.example.comprinhas.data.shoppingItem.ShoppingItemDao
import com.example.comprinhas.http.DatabaseApi
import com.example.comprinhas.http.responses.CreateListError
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

class ShoppingListRepository(
    private val shoppingItemDao: ShoppingItemDao,
    private val shoppingListDao: ShoppingListDao,
    private val context: Context
) {

    val shoppingLists: Flow<List<ShoppingList>> = shoppingListDao.getAllShoppingLists()
    val shoppingList: Flow<List<ShoppingItem>> = shoppingItemDao.getAllShopping()
    val cartList: Flow<List<ShoppingItem>> = shoppingItemDao.getAllCart()

    private val retrofitService = DatabaseApi.retrofitService

    suspend fun createShoppingList(username: String, listName: String, listPassword: String): String {
        val response = retrofitService.createList(
            username,
            listName,
            listPassword
        )

        if (response.code() == 200) {
            shoppingListDao.createShoppingList(
                ShoppingList(response.body()!!.idLista, listName, listPassword, username)
            )
            Log.d("REPOSITORY",
                "Criando Lista ${response.body()?.idLista ?: "undefined"} $listName"
            )

            return "OK"
        }
        else {
            val error = Gson().fromJson(
                response.errorBody()!!.charStream(), CreateListError::class.java
            )

            Log.w("REPOSITORY", "Erro ao criar lista: ${error.message}")
            return error.message
        }
    }

    suspend fun exitShoppingList(listName: String, listPassword: String) {
        shoppingListDao.deleteShoppingList(listName, listPassword)
    }

    suspend fun deleteList(username: String, listName: String, listPassword: String) {
        val response = retrofitService.deleteList(username, listName, listPassword)

        shoppingListDao.deleteShoppingList(listName, listPassword)
    }

    suspend fun joinShoppingList(name: String, listName: String, listPassword: String): String? {
        val response = retrofitService.joinShoppingList(name, listName, listPassword)

        if (response.code() == 200) {
            shoppingListDao.createShoppingList(
                ShoppingList(
                response.body()!!.idLista,
                listName,
                listPassword,
                response.body()!!.criadoPor
            )
            )

            return null
        }
        else {
            val error = Gson().fromJson(
                response.errorBody()!!.charStream(), CreateListError::class.java
            )

            return error.message
        }
    }

    suspend fun insert(item: ShoppingItem) {
        shoppingItemDao.insert(item)
        retrofitService.addNewItem(item.idLista, item.idItem, item.nomeItem, item.adicionadoPor)
    }

    suspend fun deleteFromList(item: ShoppingItem, lastChanged: Long) {
        shoppingItemDao.deleteFromList(item)
        retrofitService.removeItem(item.idLista, item.idItem, item.adicionadoPor, lastChanged)
    }

    suspend fun clearList() {
        shoppingItemDao.clearList()
    }

    suspend fun moveToCart(id: Long) {
        shoppingItemDao.moveToCart(id)
    }

    suspend fun removeFromCart(id: Long) {
        shoppingItemDao.removeFromCart(id)
    }

    suspend fun clearCart(idList: List<Long>, listId: Int, lastChanged: Long) {
        shoppingItemDao.clearCart()
    }
}