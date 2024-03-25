package com.example.comprinhas.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
@Entity(tableName = "items")
data class ShoppingItem(
    @PrimaryKey
    val id: Long = ZonedDateTime.now().toEpochSecond(),

    val name: String,
    val addedBy: String = "Fulano",
    val onCart: Boolean = false
)