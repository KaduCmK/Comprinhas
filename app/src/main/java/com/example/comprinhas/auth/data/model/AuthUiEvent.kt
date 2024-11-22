package com.example.comprinhas.auth.data.model

import com.example.comprinhas.auth.data.AuthService

sealed interface AuthUiEvent {
    data class OnLogin(val authService: AuthService) : AuthUiEvent
}