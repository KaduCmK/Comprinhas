package io.github.kaducmk.comprinhas.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import io.github.kaducmk.comprinhas.auth.data.AuthService
import io.github.kaducmk.comprinhas.auth.presentation.AuthScreenRoot
import io.github.kaducmk.comprinhas.core.data.model.Usuario
import io.github.kaducmk.comprinhas.home.presentation.HomeScreenRoot
import io.github.kaducmk.comprinhas.list.presentation.ShoppingListScreenRoot

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
