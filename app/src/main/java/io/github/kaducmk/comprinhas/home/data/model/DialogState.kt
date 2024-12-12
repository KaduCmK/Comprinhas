package io.github.kaducmk.comprinhas.home.data.model


sealed class DialogState{

    data object Loading : DialogState()
    data class Loaded(val searchResult: List<ShoppingList>) :
        DialogState()
}