package io.github.kaducmk.comprinhas.home.data.model

import io.github.kaducmk.comprinhas.core.data.model.Usuario

sealed class HomeUiState {
    abstract val currentUser: Usuario?
    abstract val lists: List<ShoppingList>?

    data class Loaded(
        override val currentUser: Usuario?,
        override val lists: List<ShoppingList>?,
        val dialogState: DialogState?
    ) : HomeUiState()

    data class Loading(
        override val currentUser: Usuario?,
        override val lists: List<ShoppingList>?
    ) : HomeUiState()

    data class NoInternet(
        override val currentUser: Usuario?,
        override val lists: List<ShoppingList>?
    ) : HomeUiState()

    data class Error(
        override val currentUser: Usuario?,
        override val lists: List<ShoppingList>?, val message: String
    ) : HomeUiState()
}