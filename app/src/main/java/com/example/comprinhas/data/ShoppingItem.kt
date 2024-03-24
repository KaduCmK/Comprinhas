package com.example.comprinhas.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.Date

@Entity(tableName = "items")
data class ShoppingItem(
    @PrimaryKey
    val id: ZonedDateTime = ZonedDateTime.now(),

    val name: String,
    val addedBy: String = "Fulano",
    val onCart: Boolean = false
)