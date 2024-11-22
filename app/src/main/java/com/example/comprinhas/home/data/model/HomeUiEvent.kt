package com.example.comprinhas.home.data.model

import com.example.comprinhas.home.data.ShoppingList

sealed interface HomeUiEvent {
    data object OnGetShoppingLists : HomeUiEvent
    data class OnHoldCard(val shoppingList: ShoppingList) : HomeUiEvent
}