package com.example.comprinhas.data.shoppingList

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "listaCompras")
data class ShoppingList(
    @PrimaryKey val idLista: Int,
    val nomeLista: String,
    val senhaLista: String,
    val criadoPor: String,
)
