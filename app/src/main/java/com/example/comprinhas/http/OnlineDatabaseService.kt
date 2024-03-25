package com.example.comprinhas.http

import com.example.comprinhas.data.ShoppingItem
import kotlinx.serialization.Serializable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl("http://192.168.1.75:3000")
    .build()

object DatabaseApi {
    val retrofitService: OnlineDatabaseService by lazy {
        retrofit.create(OnlineDatabaseService::class.java)
    }
}

@Serializable
data class DatabaseRequest(val lastChanged: String, val shoppingList: List<ShoppingItem>)

interface OnlineDatabaseService {

    @GET("/test")
    suspend fun getTest(@Query("idList") idList: String)

    @GET("/")
    suspend fun getDatabase(): DatabaseRequest

    @POST("/newItem")
    suspend fun addNewItem(
        @Query("id") id: Long,
        @Query("name") name: String,
        @Query("addedBy") addedBy: String
    )

    @POST("/removeItem")
    suspend fun removeItem(@Query("id") id: Long)

    @POST("/clearCart")
    suspend fun clearCart(@Query("idList") idList: List<Long>)
}