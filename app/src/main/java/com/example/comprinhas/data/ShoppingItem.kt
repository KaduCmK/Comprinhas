package com.example.comprinhas.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
@Entity(tableName = "items")
data class ShoppingItem(
    @PrimaryKey
    val idItem: Long = ZonedDateTime.now().toEpochSecond(),

    var nomeLista: String = "",
    val nomeItem: String = "item",
    val adicionadoPor: String = "Fulano",
    val onCart: Boolean = false
)