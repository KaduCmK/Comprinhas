package com.example.comprinhas.list.data.di

import android.util.Log
import com.example.comprinhas.core.data.model.Usuario
import com.example.comprinhas.home.data.di.ShoppingListService
import com.example.comprinhas.list.data.model.CartItemFirestore
import com.example.comprinhas.list.data.model.ShoppingItem
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CartService @Inject constructor() {
    suspend fun addItemToCart(listUid: String, owner: Usuario, item: ShoppingItem): Result<Unit> {
        return try {
            val fb = Firebase.firestore

            val cartRef = fb.collection("shoppingLists")
                .document(listUid).collection("carrinho")

            cartRef.add(CartItemFirestore(item = item, owner = owner)).await()
            Result.success(Unit)
        }
        catch (e: Exception) {
            Log.e("CartService", "Error adding item to cart", e)
            Result.failure(e)
        }
    }
}