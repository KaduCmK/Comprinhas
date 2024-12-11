package io.github.kaducmk.comprinhas.list.data.model

import io.github.kaducmk.comprinhas.core.data.model.Usuario
import kotlinx.serialization.Serializable

@Serializable
data class CartItemFirestore(
    val  item: ShoppingItem? = null,
    val owner: Usuario? = null
)
