package io.github.kaducmk.comprinhas.home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kaducmk.comprinhas.home.data.di.ShoppingListService
import io.github.kaducmk.comprinhas.home.data.model.DialogState
import io.github.kaducmk.comprinhas.home.data.model.HomeUiEvent
import io.github.kaducmk.comprinhas.home.data.model.HomeUiState
import io.github.kaducmk.comprinhas.home.data.model.ShoppingListFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kaducmk.comprinhas.core.data.di.QrCodeService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val shoppingListService: ShoppingListService,
    private val qrCodeService: QrCodeService
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading(null, null))
    val uiState = _uiState.asStateFlow()

    @OptIn(ExperimentalEncodingApi::class)
    fun onEvent(uiEvent: HomeUiEvent) {
        viewModelScope.launch {
            when (uiEvent) {
                is HomeUiEvent.OnDialog -> {
                    _uiState.update {
                        HomeUiState.Loaded(
                            it.currentUser,
                            it.lists ?: emptyList(),
                            if (uiEvent.newList == null) null else DialogState.Loaded(
                                emptyList()
                            )
                        )
                    }
                }

                is HomeUiEvent.OnGetShoppingLists -> {
                    _uiState.update { HomeUiState.Loading(it.currentUser, it.lists) }
                    shoppingListService.getOwnedShoppingLists(uiEvent.currentUser.uid!!)
                        .let { result ->
                            _uiState.update {
                                HomeUiState.Loaded(uiEvent.currentUser, result, null)
                            }
                        }

                }

                is HomeUiEvent.OnCreateShoppingList -> {
                    _uiState.update { HomeUiState.Loading(it.currentUser, it.lists) }
                    val shoppingList = ShoppingListFirestore(
                        criador = _uiState.value.currentUser,
                        nome = uiEvent.nome,
                        senha = uiEvent.senha,
                    )
                    shoppingListService.createShoppingList(shoppingList)
                    _uiState.update {
                        HomeUiState.Loaded(
                            it.currentUser,
                            shoppingListService.getOwnedShoppingLists(_uiState.value.currentUser?.uid!!),
                            null
                        )
                    }
                }

                is HomeUiEvent.OnJoinShoppingList -> {
                    _uiState.update { HomeUiState.Loading(it.currentUser, it.lists) }
                    shoppingListService.joinShoppingList(uiEvent.uid, uiEvent.password)
                    _uiState.update {
                        HomeUiState.Loaded(
                            it.currentUser,
                            shoppingListService.getOwnedShoppingLists(_uiState.value.currentUser?.uid!!),
                            null
                        )
                    }
                }

                is HomeUiEvent.OnSearchShoppingList -> {
                    _uiState.update { HomeUiState.Loading(it.currentUser, it.lists) }
                    val result = shoppingListService.searchShoppingList(uiEvent.nome)
                    _uiState.update {
                        HomeUiState.Loaded(
                            it.currentUser,
                            it.lists,
                            DialogState.Loaded(result)
                        )
                    }
                }

                is HomeUiEvent.OnHoldCard -> {
                    _uiState.update { HomeUiState.Loading(it.currentUser, it.lists) }
                    shoppingListService.deleteShoppingList(uiEvent.shoppingList.id)
                    _uiState.update {
                        HomeUiState.Loaded(
                            it.currentUser,
                            shoppingListService.getOwnedShoppingLists(_uiState.value.currentUser?.uid!!),
                            null
                        )
                    }
                }

                is HomeUiEvent.OnScanQrCode -> {
                    qrCodeService.scanQrCode(
                        onSuccess = { barcode ->
                            val data = Base64.decode(barcode.rawBytes!!).decodeToString()
                            val nome = data.split(":")[0]
                            val senha = data.split(":")[1]

                            onEvent(HomeUiEvent.OnJoinShoppingList(nome, senha))
                        },
                        onFailure = {
                            Log.e("HomeViewModel", "Failed to scan QR code", it)
                        }
                    )
                }
            }
        }
    }
}