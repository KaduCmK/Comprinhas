package com.example.comprinhas.list.data.model

import com.example.comprinhas.core.data.model.Usuario
import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val id: String,
    val item: ShoppingItem,
    val owner: Usuario
)