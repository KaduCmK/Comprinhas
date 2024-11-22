package com.example.comprinhas.auth.data.model

import com.example.comprinhas.core.data.model.Usuario

sealed class AuthUiState {
    data object Unauthenticated : AuthUiState()
    data object Loading : AuthUiState()
    data class Error(val message: String) : AuthUiState()
    data class Authenticated(val currentUser: Usuario) : AuthUiState()
}