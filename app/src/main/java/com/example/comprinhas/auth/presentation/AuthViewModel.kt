package com.example.comprinhas.auth.presentation

import androidx.lifecycle.ViewModel
import com.example.comprinhas.auth.data.model.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Unauthenticated)
    val uiState = _uiState.asStateFlow()
}