package com.example.comprinhas.home.data.di

import android.util.Log
import com.example.comprinhas.core.data.UsuarioService
import com.example.comprinhas.core.data.model.Usuario
import com.example.comprinhas.home.data.model.ShoppingList
import com.example.comprinhas.home.data.model.ShoppingListFirestore
import com.google.android.play.integrity.internal.s
import com.google.android.play.integrity.internal.u
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ShoppingListService @Inject constructor(
    private val usuarioService: UsuarioService
) {
    suspend fun getOwnedShoppingLists(uid: String): List<ShoppingList> {
        val db = Firebase.firestore

        val createdListsSnapshot = db.collection("shoppingLists")
            .whereEqualTo("criador.uid", uid)
            .get()
            .await()

//        val participatingListsSnapshot = db.collectionGroup("participantes")
//            .whereEqualTo("uid", uid)
//            .get()
//            .await()

        val createdLists = createdListsSnapshot.documents.mapNotNull { doc ->
            val shoppingListDto = doc.toObject(ShoppingListFirestore::class.java)

            val listaParticipantes = doc.reference.collection("participantes").get().await()
                .documents.mapNotNull { participante ->
                    participante.toObject(Usuario::class.java)
                }

            ShoppingList(
                id = doc.id,
                criador = shoppingListDto?.criador!!,
                nome = shoppingListDto.nome!!,
                senha = shoppingListDto.senha,
                imgUrl = shoppingListDto.imgUrl,
                participantes = listaParticipantes
            )
        }
        return createdLists
    }

    suspend fun createShoppingList(shoppingList: ShoppingListFirestore): Result<Unit> {
        val db = Firebase.firestore

        return try {
            val docRef = db.collection("shoppingLists").document()
            docRef.set(shoppingList).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun joinShoppingList(uid: String, currentUser: Usuario): Result<Unit> {
        val db = Firebase.firestore

        return try {
            val participantesRef = db.collection("shoppingLists")
                .document(uid)
                .collection("participantes")
            participantesRef.add(currentUser).await()

            Result.success(Unit)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }
}