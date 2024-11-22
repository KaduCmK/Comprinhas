package com.example.comprinhas.auth.data.model

import com.example.comprinhas.core.data.model.Usuario

sealed class AuthUiState {
    data class Unauthenticated(val error: String?) : AuthUiState()
    data object Loading : AuthUiState()
    data object Authenticated : AuthUiState()
}