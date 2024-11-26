package com.example.comprinhas.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object ToAuth

@Serializable
data object ToHome

@Serializable
data class ToShoppingList(val id: String)