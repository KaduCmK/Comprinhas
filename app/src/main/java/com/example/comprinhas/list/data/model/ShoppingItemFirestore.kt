package com.example.comprinhas.list.data.model

import com.example.comprinhas.core.data.model.Usuario

data class ShoppingItemFirestore(
    val id: String? = null,
    val nome: String? = null,
    val adicionadoPor: Usuario? = null,
    val onCart: Boolean? = null
)
