package com.example.comprinhas.home.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "listaCompras")
data class ShoppingList(
    @PrimaryKey val idLista: String,
    val nomeLista: String,
    val senhaLista: String,
    val criadoPor: String,
)
