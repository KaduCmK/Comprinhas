package com.example.comprinhas.http

import com.example.comprinhas.data.ShoppingItem
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl("https://comprinhas-server.fly.dev")
    .build()

object DatabaseApi {
    val retrofitService: OnlineDatabaseService by lazy {
        retrofit.create(OnlineDatabaseService::class.java)
    }
}

@Serializable
data class DatabaseRequest(val lastChanged: Long, val shoppingList: List<ShoppingItem>)

interface OnlineDatabaseService {

    @GET("/test")
    suspend fun getTest(@Query("idList") idList: String)

    @GET("/")
    suspend fun getDatabase(): Response<DatabaseRequest>

    @POST("/newItem")
    suspend fun addNewItem(
        @Query("id") id: Long,
        @Query("name") name: String,
        @Query("addedBy") addedBy: String
    )

    @POST("/removeItem")
    suspend fun removeItem(@Query("id") id: Long, @Query("lastChanged") lastChanged: Long)

    @POST("/clearCart")
    suspend fun clearCart(@Query("idList") idList: List<Long>, @Query("lastChanged") lastChanged: Long)
}