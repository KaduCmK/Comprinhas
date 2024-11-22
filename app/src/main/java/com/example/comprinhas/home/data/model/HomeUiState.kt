package com.example.comprinhas.home.data.model

import com.example.comprinhas.core.data.model.Usuario
import com.example.comprinhas.home.data.ShoppingList

sealed class HomeUiState {
    abstract val currentUser: Usuario?

    data class Loaded(override val currentUser: Usuario?, val lists: List<ShoppingList>) :
        HomeUiState()

    data class Loading(override val currentUser: Usuario?) : HomeUiState()
    data class NoInternet(override val currentUser: Usuario?) : HomeUiState()
    data class Error(override val currentUser: Usuario?, val message: String) : HomeUiState()
}