package io.github.kaducmk.comprinhas.list.data.di

import android.util.Log
import io.github.kaducmk.comprinhas.core.data.model.Usuario
import io.github.kaducmk.comprinhas.list.data.model.CartItem
import io.github.kaducmk.comprinhas.list.data.model.CartItemFirestore
import io.github.kaducmk.comprinhas.list.data.model.ShoppingItem
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CartService @Inject constructor(
) {
    private val fb = Firebase.firestore
    private val user = FirebaseAuth.getInstance().currentUser!!

    suspend fun getOwnCartItems(listUid: String): List<CartItem> {
        return try {
            val listRef = fb.collection("shoppingLists").document(listUid)

            val carrinho = listRef.collection("carrinho")
                .get().await()
                .mapNotNull { doc ->
                    val dto = doc.toObject(CartItemFirestore::class.java)

                    CartItem(
                        id = doc.id,
                        item = dto.item!!,
                        owner = dto.owner!!
                    )
                }

            carrinho
        }
        catch (e: Exception) {
            Log.e("CartService", "Error getting cart items", e)
            emptyList()
        }
    }

    suspend fun addItemToCart(listUid: String, item: ShoppingItem): Result<Unit> {
        return try {
            val listRef = fb.collection("shoppingLists").document(listUid)
            val cartRef = listRef.collection("carrinho")

            cartRef.add(CartItemFirestore(item = item, owner = Usuario(user))).await()
            listRef.collection("items").document(item.id!!).delete().await()
            Result.success(Unit)
        }
        catch (e: Exception) {
            Log.e("CartService", "Error adding item to cart", e)
            Result.failure(e)
        }
    }

    suspend fun removeItemFromCart(listUid: String, cartItem: CartItem): Result<Unit> {
        return try {
            val listRef = fb.collection("shoppingLists").document(listUid)
            val cartRef = listRef.collection("carrinho")

            cartRef.document(cartItem.id).delete().await()
            listRef.collection("items").document(cartItem.item.id!!).set(cartItem.item)
                .await()
            Result.success(Unit)
        }
        catch (e: Exception) {
            Log.e("CartService", "Error removing item from cart", e)
            Result.failure(e)
        }
    }

    suspend fun clearCart(listUid: String): Result<Unit> {
        return try {
            val listRef = fb.collection("shoppingLists").document(listUid)
            val cartRef = listRef.collection("carrinho")

            cartRef.whereEqualTo("owner.uid", user.uid).get().await().forEach { doc ->
                cartRef.document(doc.id).delete()
            }

            Result.success(Unit)
        }
        catch (e: Exception) {
            Log.e("CartService", "Error clearing cart", e)
            Result.failure(e)
        }
    }
}