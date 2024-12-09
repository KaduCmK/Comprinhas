package com.example.comprinhas.list.data.model

import com.example.comprinhas.core.data.model.Usuario
import kotlinx.serialization.Serializable

@Serializable
data class CartItemFirestore(
    val  item: ShoppingItem? = null,
    val owner: Usuario? = null
)
