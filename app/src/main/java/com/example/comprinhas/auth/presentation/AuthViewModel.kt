package com.example.comprinhas.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.comprinhas.auth.data.AuthService
import com.example.comprinhas.auth.data.model.AuthUiEvent
import com.example.comprinhas.auth.data.model.AuthUiState
import com.example.comprinhas.core.data.model.Usuario
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Unauthenticated(null))
    val uiState = _uiState.asStateFlow()

    fun onEvent(uiEvent: AuthUiEvent) {
        when (uiEvent) {
            is AuthUiEvent.OnLogin -> {
                viewModelScope.launch {
                    _uiState.emit(AuthUiState.Loading)

                    val currentUser = uiEvent.authService.getSignedInUser()
                    if (currentUser != null) {
                        _uiState.emit(AuthUiState.Authenticated)
                        return@launch
                    }

                    uiEvent.authService.googleSignIn().collect { result ->
                        result.fold(
                            onSuccess = { authResult ->
                                _uiState.emit(AuthUiState.Authenticated)
                            },
                            onFailure = { exception ->
                                _uiState.emit(
                                    AuthUiState.Unauthenticated(
                                        exception.message ?: "Erro desconhecido"
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}