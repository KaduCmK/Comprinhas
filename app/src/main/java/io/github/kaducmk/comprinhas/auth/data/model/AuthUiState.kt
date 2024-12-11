package io.github.kaducmk.comprinhas.auth.data.model

sealed class AuthUiState {
    data class Unauthenticated(val error: String?) : AuthUiState()
    data object Loading : AuthUiState()
    data object Authenticated : AuthUiState()
}