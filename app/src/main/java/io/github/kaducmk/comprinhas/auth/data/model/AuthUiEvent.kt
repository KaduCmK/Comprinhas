package io.github.kaducmk.comprinhas.auth.data.model

import io.github.kaducmk.comprinhas.auth.data.AuthService

sealed interface AuthUiEvent {
    data class OnLogin(val authService: AuthService) : AuthUiEvent
}