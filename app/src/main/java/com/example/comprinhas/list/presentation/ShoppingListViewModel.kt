package com.example.comprinhas.list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comprinhas.core.data.di.QrCodeService
import com.example.comprinhas.home.data.di.ShoppingListService
import com.example.comprinhas.list.data.di.CartService
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
    private val shoppingItemService: ShoppingItemService,
    private val cartService: CartService,
    private val qrCodeService: QrCodeService
) : ViewModel() {
    private val _uiState =
        MutableStateFlow<ShoppingListUiState>(
            ShoppingListUiState.Loading(
                null,
                null,
                emptyList(),
                emptyList()
            )
        )
    val uiState = _uiState.asStateFlow()

    fun onEvent(uiEvent: ShoppingListUiEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                is ShoppingListUiEvent.OnSetShoppingList -> {
                    viewModelScope.launch {
                        _uiState.update {
                            ShoppingListUiState.Loading(
                                currentUser = shoppingListService.currentUser,
                                shoppingList = shoppingListService.getShoppingListById(uiEvent.uid),
                                shoppingItems = shoppingItemService.getShoppingItems(uiEvent.uid),
                                carrinho = cartService.getOwnCartItems(uiEvent.uid)
                            )
                        }

                        _uiState.update {
                            ShoppingListUiState.Loaded(
                                currentUser = it.currentUser,
                                shoppingList = it.shoppingList!!,
                                shoppingItems = shoppingItemService.getShoppingItems(it.shoppingList!!.id),
                                carrinho = cartService.getOwnCartItems(it.shoppingList!!.id),
                                dialogState = false to null
                            )
                        }
                    }
                }

                is ShoppingListUiEvent.OnAddShoppingItem -> {
                    _uiState.update {
                        ShoppingListUiState.Loading(it.currentUser, it.shoppingList, it.shoppingItems, it.carrinho)
                    }
                    shoppingItemService.addShoppingItem(
                        _uiState.value.shoppingList!!.id,
                        uiEvent.nome
                    )
                    _uiState.update {
                        ShoppingListUiState.Loaded(
                            currentUser = it.currentUser,
                            shoppingList = it.shoppingList!!,
                            shoppingItems = shoppingItemService.getShoppingItems(it.shoppingList!!.id),
                            carrinho = it.carrinho,
                            dialogState = false to null
                        )
                    }
                }

                is ShoppingListUiEvent.OnDeleteShoppingItem -> {
                    _uiState.update {
                        ShoppingListUiState.Loading(it.currentUser, it.shoppingList, it.shoppingItems, it.carrinho)
                    }
                    shoppingItemService.deleteShoppingItem(
                        _uiState.value.shoppingList!!.id,
                        uiEvent.uid
                    )
                    _uiState.update {
                        ShoppingListUiState.Loaded(
                            currentUser = it.currentUser,
                            shoppingList = it.shoppingList!!,
                            shoppingItems = shoppingItemService.getShoppingItems(it.shoppingList!!.id),
                            carrinho = cartService.getOwnCartItems(it.shoppingList!!.id),
                            dialogState = false to null
                        )
                    }
                }

                is ShoppingListUiEvent.OnEditShoppingItem -> {
                    _uiState.update {
                        ShoppingListUiState.Loading(it.currentUser, it.shoppingList, it.shoppingItems, it.carrinho)
                    }
                    shoppingItemService.editShoppingItem(
                        _uiState.value.shoppingList!!.id,
                        uiEvent.uid,
                        uiEvent.nome
                    )
                    _uiState.update {
                        ShoppingListUiState.Loaded(
                            currentUser = it.currentUser,
                            shoppingList = it.shoppingList!!,
                            shoppingItems = shoppingItemService.getShoppingItems(it.shoppingList!!.id),
                            carrinho = cartService.getOwnCartItems(it.shoppingList!!.id),
                            dialogState = false to null
                        )
                    }
                }

                is ShoppingListUiEvent.OnAddToCart -> {
                    _uiState.update {
                        ShoppingListUiState.Loading(it.currentUser, it.shoppingList, it.shoppingItems, it.carrinho)
                    }
                    cartService.addItemToCart(_uiState.value.shoppingList?.id!!, uiEvent.item)
                    _uiState.update {
                        ShoppingListUiState.Loaded(
                            currentUser = it.currentUser,
                            shoppingList = it.shoppingList!!,
                            shoppingItems = shoppingItemService.getShoppingItems(it.shoppingList!!.id),
                            carrinho = cartService.getOwnCartItems(it.shoppingList!!.id),
                            dialogState = false to null,
                        )
                    }
                }

                is ShoppingListUiEvent.OnRemoveFromCart -> {
                    _uiState.update {
                        ShoppingListUiState.Loading(it.currentUser, it.shoppingList, it.shoppingItems, it.carrinho)
                    }
                    cartService.removeItemFromCart(_uiState.value.shoppingList?.id!!, uiEvent.item)
                    _uiState.update {
                        ShoppingListUiState.Loaded(
                            currentUser = it.currentUser,
                            shoppingList = it.shoppingList!!,
                            shoppingItems = shoppingItemService.getShoppingItems(it.shoppingList!!.id),
                            carrinho = cartService.getOwnCartItems(it.shoppingList!!.id),
                            dialogState = false to null
                        )
                    }
                }

                is ShoppingListUiEvent.OnClearCart -> {
                    _uiState.update {
                        ShoppingListUiState.Loading(it.currentUser, it.shoppingList, it.shoppingItems, it.carrinho)
                    }
                    cartService.clearCart(_uiState.value.shoppingList?.id!!)
                    _uiState.update {
                        ShoppingListUiState.Loaded(
                            currentUser = it.currentUser,
                            shoppingList = it.shoppingList!!,
                            shoppingItems = shoppingItemService.getShoppingItems(it.shoppingList!!.id),
                            carrinho = cartService.getOwnCartItems(it.shoppingList!!.id),
                            dialogState = false to null
                        )
                    }
                }

                is ShoppingListUiEvent.OnToggleDialog -> {
                    _uiState.update {
                        ShoppingListUiState.Loaded(
                            currentUser = it.currentUser,
                            shoppingList = it.shoppingList!!,
                            shoppingItems = it.shoppingItems,
                            carrinho = it.carrinho,
                            dialogState = uiEvent.dialog to uiEvent.editItem
                        )
                    }
                }

                is ShoppingListUiEvent.OnToggleQrCode -> {
                    _uiState.update {
                        ShoppingListUiState.Loaded(
                            currentUser = it.currentUser,
                            shoppingList = it.shoppingList!!,
                            shoppingItems = it.shoppingItems,
                            carrinho = it.carrinho,
                            dialogState = false to null,
                            qrCode = if (uiEvent.toggle)
                                qrCodeService.generateQrCode("${it.shoppingList!!.id}:${it.shoppingList!!.senha}")
                            else
                                null
                        )
                    }
                }
            }
        }
    }
}