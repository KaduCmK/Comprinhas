package com.example.comprinhas.http

import com.example.comprinhas.data.ShoppingItem
import com.example.comprinhas.ui.receipts.Receipt
import kotlinx.serialization.Serializable
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private val client = OkHttpClient.Builder()
    .connectTimeout(10, TimeUnit.SECONDS)
    .readTimeout(90, TimeUnit.SECONDS)
    .writeTimeout(90, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl("https://comprinhas-server.fly.dev")
    .client(client)
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
    ): Response<ResponseBody>

    @GET("/receipts/get/username")
    suspend fun getReceiptsByUsername(
        @Query("username") username: String
    ): Response<List<Receipt>>

    @POST("/createList")
    suspend fun createList(
        @Query("username") username: String,
        @Query("listName") listName: String,
        @Query("listPassword") listPassword: String
    ): Response<ResponseBody>

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