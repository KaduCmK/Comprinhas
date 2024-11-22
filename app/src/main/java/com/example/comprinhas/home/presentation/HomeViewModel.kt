package com.example.comprinhas.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comprinhas.core.data.model.Usuario
import com.example.comprinhas.home.data.model.HomeUiEvent
import com.example.comprinhas.home.data.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading(null))
    val uiState = _uiState.asStateFlow()

    fun onEvent(uiEvent: HomeUiEvent) {
        when (uiEvent) {
            is HomeUiEvent.OnGetShoppingLists -> {
                viewModelScope.launch {
                    _uiState.update {
                        HomeUiState.Loaded(it.currentUser, emptyList())
                    }
                }
            }
            is HomeUiEvent.OnHoldCard -> {

            }
        }
    }

    fun setCurrentUser(user: Usuario?) {
        viewModelScope.launch { _uiState.emit(HomeUiState.Loading(user)) }
    }
}