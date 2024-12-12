package io.github.kaducmk.comprinhas.ui.navigation

import kotlinx.serialization.Serializable
import java.io.Serial

@Serializable
sealed class HomeRoutes {
    @Serializable
    data object ToHomeScreen : HomeRoutes()

    @Serializable
    data object ToJoinListScreen : HomeRoutes()

    @Serializable
    data object ToCreateListScreen : HomeRoutes()

    @Serializable
    data class ToShoppingList(val id: String)
}