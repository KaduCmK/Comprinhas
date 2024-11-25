package com.example.comprinhas.home.data.model

import androidx.room.PrimaryKey
import com.example.comprinhas.core.data.model.Usuario

data class ShoppingListFirestore(
    val nome: String? = null,
    val senha: String? = null,
    val criador: Usuario? = null,
    val imgUrl: String? = null
)
