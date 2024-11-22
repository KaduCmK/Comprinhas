package com.example.comprinhas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.comprinhas.auth.presentation.AuthScreen

@Composable
fun MainNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Auth) {
        composable<Auth> {
            AuthScreen()
        }

//        composable<Home> {
//            HomeScreen()
//        }
    }
}
