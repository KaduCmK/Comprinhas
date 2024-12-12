package io.github.kaducmk.comprinhas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.github.kaducmk.comprinhas.auth.data.AuthService
import io.github.kaducmk.comprinhas.auth.presentation.AuthScreenRoot

@Composable
fun MainNavGraph(navController: NavHostController, authService: AuthService) {
    NavHost(navController = navController, startDestination = ToHomeNavGraph) {
        composable<ToAuth> {
            AuthScreenRoot(authService = authService, navController = navController)
        }

        homeNavGraph(navController, authService)
    }
}
