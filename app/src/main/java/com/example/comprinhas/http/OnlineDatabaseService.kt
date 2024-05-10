package com.example.comprinhas.http

import com.example.comprinhas.data.ShoppingItem
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl("http://192.168.100.100:8080")
    .build()

object DatabaseApi {
    val retrofitService: OnlineDatabaseService by lazy {
        retrofit.create(OnlineDatabaseService::class.java)
    }
}

@Serializable
data class ListaCompras(val lastChanged: Long, val shoppingList: List<ShoppingItem>)

interface OnlineDatabaseService {

    @POST("/newReceipt")
    suspend fun newReceipt(
        @Body body: BodyRequest
    )

    @GET("/")
    suspend fun getDatabase(
        @Query("name") name: String,
        @Query("listName") listId: String,
        @Query("listPassword") listPassword: String
    ): Response<ListaCompras>

    @POST("/newItem")
    suspend fun addNewItem(
        @Query("listName") listName: String,
        @Query("itemId") id: Long,
        @Query("itemName") name: String,
        @Query("addedBy") addedBy: String
    )

    @POST("/removeItem")
    suspend fun removeItem(
        @Query("listName") listName: String,
        @Query("itemId") itemId: Long,
        @Query("addedBy") addedBy: String,
        @Query("lastChanged") lastChanged: Long
    )

    @POST("/clearCart")
    suspend fun clearCart(
        @Query("idList") idList: List<Long>,
        @Query("listName") listName: String,
        @Query("lastChanged") lastChanged: Long)
}