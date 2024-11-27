package com.example.comprinhas.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comprinhas.home.data.di.ShoppingListService
import com.example.comprinhas.list.data.di.ShoppingItemService
import com.example.comprinhas.list.data.model.ShoppingListUiEvent
import com.example.comprinhas.list.data.model.ShoppingListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val shoppingListService: ShoppingListService,
    private val shoppingItemService: ShoppingItemService
) : ViewModel() {
    private val _uiState = MutableStateFlow<ShoppingListUiState>(ShoppingListUiState.Loading(null))
    val uiState = _uiState.asStateFlow()

    fun onEvent(uiEvent: ShoppingListUiEvent) {
        when (uiEvent) {
            is ShoppingListUiEvent.OnSetShoppingList -> {
                viewModelScope.launch {
                    _uiState.update {
                        ShoppingListUiState.Loading(
                            shoppingList = shoppingListService.getShoppingListById(uiEvent.uid)
                        )
                    }

                    _uiState.update {
                        ShoppingListUiState.Loaded(
                            shoppingList = it.shoppingList!!,
                            shoppingItems = shoppingItemService.getShoppingItems(it.shoppingList!!.id),
                            dialogState = false
                        )
                    }
                }
            }

            is ShoppingListUiEvent.OnAddShoppingItem -> {
                viewModelScope.launch {
                    _uiState.update {
                        ShoppingListUiState.Loading(it.shoppingList)
                    }
                    shoppingItemService.addShoppingItem(_uiState.value.shoppingList!!.id, uiEvent.nome)
                    _uiState.update { ShoppingListUiState.Loaded(
                        shoppingList = it.shoppingList!!,
                        shoppingItems = shoppingItemService.getShoppingItems(it.shoppingList!!.id),
                        dialogState = false
                    ) }
                }
            }

            is ShoppingListUiEvent.OnToggleDialog -> {
                _uiState.update {
                    ShoppingListUiState.Loaded(
                        shoppingList = it.shoppingList!!,
                        shoppingItems = (it as? ShoppingListUiState.Loaded)?.shoppingItems ?: emptyList(),
                        dialogState = uiEvent.dialog
                    )
                }
            }
        }
    }
}