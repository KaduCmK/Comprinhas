package io.github.kaducmk.comprinhas.home.data
//
//import android.content.Context
//import android.util.Log
//import io.github.kaducmk.comprinhas.list.data.model.ShoppingItem
//import io.github.kaducmk.comprinhas.list.data.model.ShoppingItemDao
//import io.github.kaducmk.comprinhas.core.data.http.DatabaseApi
//import io.github.kaducmk.comprinhas.core.data.http.responses.CreateListError
//import io.github.kaducmk.comprinhas.home.data.model.ShoppingList
//import com.google.gson.Gson
//import kotlinx.coroutines.flow.Flow
//
//class ShoppingListRepository(
//    private val shoppingItemDao: ShoppingItemDao,
//    private val shoppingListDao: ShoppingListDao,
//    private val context: Context
//) {
//
//    val lists: Flow<List<ShoppingList>> = shoppingListDao.getAllShoppingLists()
//    val list: Flow<List<ShoppingItem>> = shoppingItemDao.getAllShopping()
//    val cartList: Flow<List<ShoppingItem>> = shoppingItemDao.getAllCart()
//
//    private val retrofitService = DatabaseApi.retrofitService
//
//    suspend fun createShoppingList(username: String, listName: String, listPassword: String): String {
//        val response = retrofitService.createList(
//            username,
//            listName,
//            listPassword
//        )
//
//        if (response.code() == 200) {
//            shoppingListDao.createShoppingList(
//                ShoppingList(response.body()!!.idLista.toString(), listName, listPassword, username)
//            )
//            Log.d("REPOSITORY",
//                "Criando Lista ${response.body()?.idLista ?: "undefined"} $listName"
//            )
//
//            return "OK"
//        }
//        else {
//            val error = Gson().fromJson(
//                response.errorBody()!!.charStream(), CreateListError::class.java
//            )
//
//            Log.w("REPOSITORY", "Erro ao criar lista: ${error.message}")
//            return error.message
//        }
//    }
//
//    suspend fun exitShoppingList(listName: String, listPassword: String) {
//        shoppingListDao.deleteShoppingList(listName, listPassword)
//    }
//
//    suspend fun deleteList(username: String, listName: String, listPassword: String) {
//        val response = retrofitService.deleteList(username, listName, listPassword)
//
//        shoppingListDao.deleteShoppingList(listName, listPassword)
//    }
//
//    suspend fun joinShoppingList(name: String, listName: String, listPassword: String): String? {
//        val response = retrofitService.joinShoppingList(name, listName, listPassword)
//
//        if (response.code() == 200) {
//            shoppingListDao.createShoppingList(
//                ShoppingList(
//                response.body()!!.idLista.toString(),
//                listName,
//                listPassword,
//                response.body()!!.criadoPor
//            )
//            )
//
//            return null
//        }
//        else {
//            val error = Gson().fromJson(
//                response.errorBody()!!.charStream(), CreateListError::class.java
//            )
//
//            return error.message
//        }
//    }
//
//    suspend fun insert(item: ShoppingItem) {
//        shoppingItemDao.insert(item)
//        retrofitService.addNewItem(item.idLista, item.idItem, item.nomeItem, item.adicionadoPor)
//    }
//
//    suspend fun deleteFromList(item: ShoppingItem, lastChanged: Long) {
//        shoppingItemDao.deleteFromList(item)
//        retrofitService.removeItem(item.idLista, item.idItem, item.adicionadoPor, lastChanged)
//    }
//
//    suspend fun clearList() {
//        shoppingItemDao.clearList()
//    }
//
//    suspend fun moveToCart(id: Long) {
//        shoppingItemDao.moveToCart(id)
//    }
//
//    suspend fun removeFromCart(id: Long) {
//        shoppingItemDao.removeFromCart(id)
//    }
//
//    suspend fun clearCart(idList: List<Long>, listId: Int, lastChanged: Long) {
//        shoppingItemDao.clearCart()
//    }
//}