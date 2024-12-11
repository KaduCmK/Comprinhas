package io.github.kaducmk.comprinhas.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kaducmk.comprinhas.core.data.UsuarioService
import io.github.kaducmk.comprinhas.home.data.di.ShoppingListService
import io.github.kaducmk.comprinhas.home.data.model.DialogState
import io.github.kaducmk.comprinhas.home.data.model.HomeUiEvent
import io.github.kaducmk.comprinhas.home.data.model.HomeUiState
import io.github.kaducmk.comprinhas.home.data.model.ShoppingListFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
            is HomeUiEvent.OnDialog -> {
                _uiState.update {
                    HomeUiState.Loaded(
                        it.currentUser,
                        (it as? HomeUiState.Loaded)?.lists ?: emptyList(),
                        if (uiEvent.newList == null) null else DialogState.Loaded(
                            uiEvent.newList,
                            emptyList()
                        )
                    )
                }
            }

            is HomeUiEvent.OnGetShoppingLists -> {
                viewModelScope.launch {
                    _uiState.update { HomeUiState.Loading(it.currentUser) }
                    shoppingListService.getOwnedShoppingLists(uiEvent.currentUser.uid!!)
                        .let { result ->
                            _uiState.update {
                                HomeUiState.Loaded(uiEvent.currentUser, result, null)
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
                    shoppingListService.joinShoppingList(
                        uiEvent.uid,
                        uiEvent.password,
                        _uiState.value.currentUser!!
                    )
                    _uiState.update {
                        HomeUiState.Loaded(
                            it.currentUser,
                            shoppingListService.getOwnedShoppingLists(_uiState.value.currentUser?.uid!!),
                            null
                        )
                    }
                }
            }

            is HomeUiEvent.OnSearchShoppingList -> {
                viewModelScope.launch {
                    val result = shoppingListService.searchShoppingList(uiEvent.nome)
                    _uiState.update {
                        HomeUiState.Loaded(
                            (it as HomeUiState.Loaded).currentUser,
                            it.lists,
                            DialogState.Loaded(it.dialogState!!.newList, result)
                        )
                    }
                }

            }

            is HomeUiEvent.OnHoldCard -> {
                viewModelScope.launch {
                    _uiState.emit(HomeUiState.Loading(_uiState.value.currentUser))
                    shoppingListService.deleteShoppingList(uiEvent.shoppingList.id)
                    _uiState.update {
                        HomeUiState.Loaded(
                            it.currentUser,
                            shoppingListService.getOwnedShoppingLists(_uiState.value.currentUser?.uid!!),
                            null
                        )
                    }

                }
            }
        }
    }
}