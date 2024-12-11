package io.github.kaducmk.comprinhas.home.data.di

import io.github.kaducmk.comprinhas.core.data.model.Usuario
import io.github.kaducmk.comprinhas.home.data.model.ShoppingList
import io.github.kaducmk.comprinhas.home.data.model.ShoppingListFirestore
import io.github.kaducmk.comprinhas.list.data.model.CartItem
import io.github.kaducmk.comprinhas.list.data.model.CartItemFirestore
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ShoppingListService @Inject constructor() {
    val currentUser = Usuario(Firebase.auth.currentUser)
    private val db = Firebase.firestore

    suspend fun getShoppingListById(listUid: String): ShoppingList {

        val shoppingListSnapshot = db.collection("shoppingLists")
            .document(listUid)
            .get()
            .await()

        val shoppingList = shoppingListSnapshot.toObject(ShoppingListFirestore::class.java)
        val listaParticipantes = shoppingListSnapshot
            .reference.collection("participantes").get().await()
            .documents.mapNotNull { participante ->
                participante.toObject(Usuario::class.java)
            }

        val carrinho = shoppingListSnapshot.reference.collection("carrinho").get().await()
            .documents.mapNotNull { item ->
                val dto = item.toObject(CartItemFirestore::class.java)

                CartItem(
                    id = item.id,
                    item = dto!!.item!!,
                    owner = dto.owner!!
                )
            }

        return ShoppingList(
            id = shoppingListSnapshot.id,
            criador = shoppingList?.criador!!,
            nome = shoppingList.nome!!,
            senha = shoppingList.senha,
            imgUrl = shoppingList.imgUrl,
            participantes = listaParticipantes,
            carrinho = carrinho
        )
    }

    suspend fun getOwnedShoppingLists(uid: String): List<ShoppingList> {

        val createdListsSnapshot = db.collection("shoppingLists")
            .whereEqualTo("criador.uid", uid)
            .get()
            .await()

        val participatingListsSnapshot = db.collectionGroup("participantes")
            .whereEqualTo("uid", uid)
            .get()
            .await()

        val createdLists = createdListsSnapshot.documents.mapNotNull { doc ->
            val shoppingListDto = doc.toObject(ShoppingListFirestore::class.java)

            val listaParticipantes = doc.reference.collection("participantes").get().await()
                .documents.mapNotNull { participante ->
                    participante.toObject(Usuario::class.java)
                }

            val carrinho = doc.reference.collection("carrinho").get().await()
                .documents.mapNotNull { item ->
                    val dto = item.toObject(CartItemFirestore::class.java)

                    CartItem(
                        id = item.id,
                        item = dto!!.item!!,
                        owner = dto.owner!!
                    )
                }

            ShoppingList(
                id = doc.id,
                criador = shoppingListDto?.criador!!,
                nome = shoppingListDto.nome!!,
                senha = shoppingListDto.senha,
                imgUrl = shoppingListDto.imgUrl,
                participantes = listaParticipantes,
                carrinho = carrinho
            )
        }

        val participatingLists = participatingListsSnapshot.documents.mapNotNull { doc ->
            val shoppingListRef = doc.reference.parent.parent
            val shoppingListDto =
                shoppingListRef?.get()?.await()?.toObject(ShoppingListFirestore::class.java)

            val listaParticipantes = shoppingListRef?.collection("participantes")?.get()?.await()
                ?.documents?.mapNotNull { participante ->
                    participante.toObject(Usuario::class.java)
                }

            val carrinho = doc.reference.collection("carrinho").get().await()
                .documents.mapNotNull { item ->
                    val dto = item.toObject(CartItemFirestore::class.java)

                    CartItem(
                        id = item.id,
                        item = dto!!.item!!,
                        owner = dto.owner!!
                    )
                }

            ShoppingList(
                id = shoppingListRef!!.id,
                criador = shoppingListDto?.criador!!,
                nome = shoppingListDto.nome!!,
                senha = shoppingListDto.senha,
                imgUrl = shoppingListDto.imgUrl,
                participantes = listaParticipantes ?: emptyList(),
                carrinho = carrinho
            )
        }

        return createdLists + participatingLists
    }

    suspend fun searchShoppingList(nome: String): List<ShoppingList> {

        val shoppingListsSnapshot = db.collection("shoppingLists")
            .whereGreaterThanOrEqualTo("nome", nome)
            .whereLessThanOrEqualTo("nome", nome + "\uf7ff")
            .get()
            .await()

        val shoppingLists = shoppingListsSnapshot.documents.mapNotNull { doc ->
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
        return shoppingLists
    }

    suspend fun createShoppingList(shoppingList: ShoppingListFirestore): Result<Unit> {

        return try {
            val docRef = db.collection("shoppingLists").document()
            docRef.set(shoppingList).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun joinShoppingList(
        uid: String,
        password: String,
        currentUser: Usuario
    ): Result<Unit> {
        return try {
            val shoppingListRef = db.collection("shoppingLists").document(uid)
            val participantesRef = shoppingListRef
                .collection("participantes")

            val shoppingList =
                shoppingListRef.get().await().toObject(ShoppingListFirestore::class.java)

            if (shoppingList?.senha == password)
                participantesRef.add(currentUser).await()
            else throw Exception("Senha incorreta")

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteShoppingList(uid: String): Result<Unit> {
        return try {
            db.collection("shoppingLists").document(uid).delete().await()
            Result.success(Unit)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }
}