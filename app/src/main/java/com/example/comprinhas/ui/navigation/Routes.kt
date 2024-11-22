package com.example.comprinhas.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes

@Serializable
data object Auth : Routes()

@Serializable
data object Home : Routes()