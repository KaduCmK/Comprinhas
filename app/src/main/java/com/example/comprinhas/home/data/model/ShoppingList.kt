package com.example.comprinhas.home.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.comprinhas.core.data.model.Usuario
import com.example.comprinhas.list.data.model.CartItem
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
