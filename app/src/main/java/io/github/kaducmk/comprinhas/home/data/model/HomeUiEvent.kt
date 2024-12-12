package io.github.kaducmk.comprinhas.home.data.model

import io.github.kaducmk.comprinhas.core.data.model.Usuario

sealed interface HomeUiEvent {
    data class OnDialog(val newList: Boolean?) : HomeUiEvent
    data class OnCreateShoppingList(val nome: String, val senha: String) : HomeUiEvent
    data class OnJoinShoppingList(val uid: String, val password: String) : HomeUiEvent
    data class OnGetShoppingLists(val currentUser: Usuario) : HomeUiEvent
    data class OnSearchShoppingList(val nome: String) : HomeUiEvent
    data class OnHoldCard(val shoppingList: ShoppingList) : HomeUiEvent
    data object OnScanQrCode : HomeUiEvent
}