package com.example.comprinhas.list.data.model

import com.example.comprinhas.core.data.model.Usuario
import kotlinx.serialization.Serializable

@Serializable
//@Entity(
//    tableName = "items",
//    foreignKeys = [ForeignKey(
//        entity = ShoppingList::class,
//        parentColumns = ["idLista"],
//        childColumns = ["idLista"],
//        onDelete = ForeignKey.CASCADE
//    )]
//)
data class ShoppingItem(
//    @PrimaryKey
    val id: String,
    val nome: String,
    val adicionadoPor: Usuario,
    val onCart: Boolean = false
)