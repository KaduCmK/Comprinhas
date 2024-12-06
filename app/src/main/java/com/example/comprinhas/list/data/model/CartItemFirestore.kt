package com.example.comprinhas.list.data.model

import com.example.comprinhas.core.data.model.Usuario

data class CartItemFirestore(
    val  item: ShoppingItem,
    val owner: Usuario
)
