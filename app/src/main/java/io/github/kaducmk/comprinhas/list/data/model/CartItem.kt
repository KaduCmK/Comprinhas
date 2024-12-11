package io.github.kaducmk.comprinhas.list.data.model

import io.github.kaducmk.comprinhas.core.data.model.Usuario
import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val id: String,
    val item: ShoppingItem,
    val owner: Usuario
)