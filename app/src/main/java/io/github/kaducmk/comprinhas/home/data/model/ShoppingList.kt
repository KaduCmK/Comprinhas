package io.github.kaducmk.comprinhas.home.data.model

import io.github.kaducmk.comprinhas.core.data.model.Usuario
import io.github.kaducmk.comprinhas.list.data.model.CartItem
import kotlinx.serialization.Serializable

@Serializable
data class ShoppingList(
    val id: String,
    val nome: String,
    val senha: String? = null,
    val criador: Usuario,
    val imgUrl: String? = null,
    val participantes: List<Usuario>,
    val carrinho: List<CartItem> = emptyList()
)
