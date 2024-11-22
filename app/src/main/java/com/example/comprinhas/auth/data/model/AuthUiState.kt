package com.example.comprinhas.auth.data.model

sealed class AuthUiState {
    data object Unauthenticated : AuthUiState()
    data object Loading : AuthUiState()
    data class Error(val message: String) : AuthUiState()
    data object Authenticated : AuthUiState()
}