package com.example.comprinhas.home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comprinhas.core.data.UsuarioService
import com.example.comprinhas.core.data.model.Usuario
import com.example.comprinhas.home.data.di.ShoppingListService
import com.example.comprinhas.home.data.model.HomeUiEvent
import com.example.comprinhas.home.data.model.HomeUiState
import com.example.comprinhas.home.data.model.ShoppingList
import com.example.comprinhas.home.data.model.ShoppingListFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val usuarioService: UsuarioService,
    private val shoppingListService: ShoppingListService
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading(null))
    val uiState = _uiState.asStateFlow()

    fun onEvent(uiEvent: HomeUiEvent) {
        when (uiEvent) {
            is HomeUiEvent.OnGetShoppingLists -> {
                viewModelScope.launch {
                    _uiState.update { HomeUiState.Loading(it.currentUser) }
                    shoppingListService.getOwnedShoppingLists(uiEvent.currentUser.uid!!)
                        .let { result ->
                            _uiState.update {
                                HomeUiState.Loaded(uiEvent.currentUser, result)
                            }
                        }
                }
            }

            is HomeUiEvent.OnCreateShoppingList -> {
                viewModelScope.launch {
                    _uiState.update { HomeUiState.Loading(it.currentUser) }
                    val shoppingList = ShoppingListFirestore(
                        criador = _uiState.value.currentUser,
                        nome = uiEvent.nome,
                        senha = uiEvent.senha,
                    )
                    shoppingListService.createShoppingList(shoppingList)
                    _uiState.update {
                        (it as HomeUiState.Loaded).copy(
                            lists = shoppingListService.getOwnedShoppingLists(_uiState.value.currentUser?.uid!!)
                        )
                    }
                }
            }

            is HomeUiEvent.OnJoinShoppingList -> {
                viewModelScope.launch {
                    _uiState.update { HomeUiState.Loading(it.currentUser) }
                    shoppingListService.joinShoppingList(uiEvent.uid, _uiState.value.currentUser!!)
                    _uiState.update {
                        (it as HomeUiState.Loaded).copy(
                            lists = shoppingListService.getOwnedShoppingLists(_uiState.value.currentUser?.uid!!)
                        )
                    }
                }
            }

            is HomeUiEvent.OnHoldCard -> {}
        }
    }
}