package com.example.comprinhas.list.data.di

import android.util.Log
import com.example.comprinhas.core.data.model.Usuario
import com.example.comprinhas.list.data.model.ShoppingItem
import com.example.comprinhas.list.data.model.ShoppingItemFirestore
import com.google.firebase.auth.FirebaseAuth
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
                )
            }

            items
        }
        catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addShoppingItem(listUid: String, nome: String): Result<Unit> {
        val fb = Firebase.firestore

        return try {
            val shoppingItemFirestore = ShoppingItemFirestore(
                nome = nome,
                adicionadoPor = Usuario(FirebaseAuth.getInstance().currentUser!!),
            )

            val itemsRef = fb.collection("shoppingLists")
                .document(listUid)
                .collection("items")
                .document()

            itemsRef.set(shoppingItemFirestore).await()

            Result.success(Unit)
        }
        catch (e: Exception) {
            Log.d("ShoppingItemService", "Error adding item", e)
            Result.failure(e)
        }
    }

    suspend fun deleteShoppingItem(listUid: String, itemUid: String): Result<Unit> {
        val fb = Firebase.firestore

        return try {
            fb.collection("shoppingLists")
                .document(listUid)
                .collection("items")
                .document(itemUid)
                .delete()
                .await()

            Result.success(Unit)
        }
        catch (e: Exception) {
            Log.d("ShoppingItemService", "Error deleting item", e)
            Result.failure(e)
        }
    }

    suspend fun editShoppingItem(listUid: String, itemUid: String, nome: String): Result<Unit> {
        val fb = Firebase.firestore

        return try {
            val itemRef = fb.collection("shoppingLists")
                .document(listUid)
                .collection("items")
                .document(itemUid)

            itemRef.update("nome", nome).await()

            Result.success(Unit)
        }
        catch (e: Exception) {
            Log.d("ShoppingItemService", "Error editing item", e)
            Result.failure(e)
        }
    }
}