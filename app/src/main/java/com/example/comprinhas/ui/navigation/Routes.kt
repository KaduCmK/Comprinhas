package com.example.comprinhas.ui.navigation

sealed class Routes {
    sealed class Auth() : Routes() {
        data object Username : Auth()
    }
    data object Home : Routes()
}