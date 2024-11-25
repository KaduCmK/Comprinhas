package com.example.comprinhas.home.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.comprinhas.core.data.model.Usuario
import kotlinx.serialization.Serializable

@Serializable
//@Entity(tableName = "shoppingLists")
data class ShoppingList(
    val id: String,
    val nome: String,
    val senha: String,
    val criador: Usuario,
    val imgUrl: String? = null,
    val participantes: List<Usuario>

)
