package io.github.kaducmk.comprinhas.home.data.model


sealed class DialogState{
    abstract val newList: Boolean

    data class Loading(override val newList: Boolean) : DialogState()
    data class Loaded(override val newList: Boolean, val searchResult: List<ShoppingList>) :
        DialogState()
}