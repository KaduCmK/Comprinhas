package com.example.comprinhas.list.data.di

import android.util.Log
import com.example.comprinhas.list.data.model.ShoppingItem
import com.example.comprinhas.list.data.model.ShoppingItemFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ShoppingItemService @Inject constructor() {
    suspend fun getShoppingItems(listUid: String) : List<ShoppingItem> {
        val fb = Firebase.firestore

        return try {
            val itemsSnapshot = fb.collection("shoppingLists")
                .document(listUid)
                .collection("items")
                .get()
                .await()

            val items = itemsSnapshot.documents.mapNotNull { doc ->
                val dto = doc.toObject(ShoppingItemFirestore::class.java)

                ShoppingItem(
                    id = doc.id,
                    nome = dto!!.nome!!,
                    adicionadoPor = dto.adicionadoPor!!,
                    onCart = dto.onCart ?: false
                )
            }

            items
        }
        catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addShoppingItem(listUid: String, shoppingItem: ShoppingItem): Result<Unit> {
        val fb = Firebase.firestore

        return try {
            val shoppingItemFirestore = ShoppingItemFirestore(
                nome = shoppingItem.nome,
                adicionadoPor = shoppingItem.adicionadoPor,
                onCart = shoppingItem.onCart
            )

            fb.collection("shoppingLists")
                .document(listUid)
                .collection("items")
                .add(shoppingItemFirestore)
                .await()

            Result.success(Unit)
        }
        catch (e: Exception) {
            Log.d("ShoppingItemService", "Error adding item", e)
            Result.failure(e)
        }
    }
}