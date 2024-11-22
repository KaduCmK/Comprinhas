package com.example.comprinhas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.comprinhas.auth.data.AuthService
import com.example.comprinhas.auth.presentation.AuthScreenRoot

@Composable
fun MainNavGraph(navController: NavHostController, authService: AuthService) {
    NavHost(navController = navController, startDestination = Auth) {
        composable<Auth> {
            AuthScreenRoot(authService = authService)
        }

//        composable<Home> {
//            HomeScreen()
//        }
    }
}
