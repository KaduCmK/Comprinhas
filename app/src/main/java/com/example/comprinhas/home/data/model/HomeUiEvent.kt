package com.example.comprinhas.home.data.model

import com.example.comprinhas.core.data.model.Usuario

sealed interface HomeUiEvent {
    data class OnCreateShoppingList(val nome: String, val senha: String) : HomeUiEvent
    data class OnGetShoppingLists(val currentUser: Usuario) : HomeUiEvent
    data class OnHoldCard(val shoppingList: ShoppingList) : HomeUiEvent
}