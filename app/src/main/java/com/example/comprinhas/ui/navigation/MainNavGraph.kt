package com.example.comprinhas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.comprinhas.auth.presentation.AuthScreen

@Composable
fun MainNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Home) {
        navigation<Routes.Auth>(startDestination = Routes.Auth.Username) {
            composable<Routes.Auth.Username> {
                AuthScreen()
            }
        }
    }
}