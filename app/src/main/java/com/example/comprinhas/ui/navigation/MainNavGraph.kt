package com.example.comprinhas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.comprinhas.auth.data.AuthService
import com.example.comprinhas.auth.presentation.AuthScreenRoot
import com.example.comprinhas.core.data.model.Usuario
import com.example.comprinhas.home.presentation.HomeScreenRoot
import com.example.comprinhas.list.presentation.ShoppingListScreenRoot

@Composable
fun MainNavGraph(navController: NavHostController, authService: AuthService) {
    NavHost(navController = navController, startDestination = ToHome) {
        composable<ToAuth> {
            AuthScreenRoot(authService = authService, navController = navController)
        }

        composable<ToHome> {
            HomeScreenRoot(
                navController = navController,
                currentUser = authService.getSignedInUser()?.let { fu -> Usuario(fu) }
            )
        }

        composable<ToShoppingList> {
            val args = it.toRoute<ToShoppingList>()
            ShoppingListScreenRoot(navController = navController, listId = args.id)
        }
    }
}
