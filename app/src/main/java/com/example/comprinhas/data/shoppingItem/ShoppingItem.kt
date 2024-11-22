package com.example.comprinhas.data.shoppingItem

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.comprinhas.home.data.ShoppingList
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
@Entity(
    tableName = "items",
    foreignKeys = [ForeignKey(
        entity = ShoppingList::class,
        parentColumns = ["idLista"],
        childColumns = ["idLista"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ShoppingItem(
    @PrimaryKey
    val idItem: Long = ZonedDateTime.now().toEpochSecond(),
    var nomeLista: String = "",
    val nomeItem: String = "item",
    val adicionadoPor: String = "Fulano",
    val idLista: Int = -1,
    val onCart: Boolean = false
)