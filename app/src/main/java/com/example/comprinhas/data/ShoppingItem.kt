package com.example.comprinhas.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val addedBy: String = "Fulano",
    val onCart: Boolean = false
)