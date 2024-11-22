package com.example.comprinhas.ui.navigation

import com.example.comprinhas.core.data.model.Usuario
import kotlinx.serialization.Serializable

@Serializable
data object Auth

@Serializable
data object Home

@Serializable
data class ShoppingList(val id: String)