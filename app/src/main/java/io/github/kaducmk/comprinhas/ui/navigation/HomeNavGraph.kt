package io.github.kaducmk.comprinhas.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import io.github.kaducmk.comprinhas.auth.data.AuthService
import io.github.kaducmk.comprinhas.core.data.model.Usuario
import io.github.kaducmk.comprinhas.home.presentation.HomeViewModel
import io.github.kaducmk.comprinhas.home.presentation.screens.CreateListScreen
import io.github.kaducmk.comprinhas.home.presentation.screens.HomeScreen
import io.github.kaducmk.comprinhas.home.presentation.screens.JoinListScreen
import io.github.kaducmk.comprinhas.list.presentation.ShoppingListScreenRoot
import kotlinx.serialization.Serializable

@Serializable
object ToHomeNavGraph

fun NavGraphBuilder.homeNavGraph(navController: NavController, authService: AuthService) {
    navigation<ToHomeNavGraph>(startDestination = HomeRoutes.ToHomeScreen) {
        composable<HomeRoutes.ToHomeScreen> { backStack ->
            val parent = remember(backStack) { navController.getBackStackEntry<ToHomeNavGraph>() }
            val viewModel = hiltViewModel<HomeViewModel>(parent)
            HomeScreen(
                navController = navController,
                currentUser = authService.getSignedInUser()?.let { fu -> Usuario(fu) },
                uiState = viewModel.uiState.collectAsState().value,
                uiEvent = viewModel::onEvent
            )
        }

        composable<HomeRoutes.ToCreateListScreen> { backStack ->
            val parent = remember(backStack) { navController.getBackStackEntry<ToHomeNavGraph>() }
            val viewModel = hiltViewModel<HomeViewModel>(parent)
            CreateListScreen(
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 4.dp),
                uiState = viewModel.uiState.collectAsState().value,
                uiEvent = viewModel::onEvent,
                navController = navController
            )
        }

        composable<HomeRoutes.ToJoinListScreen> { backStack ->
            val parent = remember(backStack) { navController.getBackStackEntry<ToHomeNavGraph>() }
            val viewModel = hiltViewModel<HomeViewModel>(parent)
            JoinListScreen(
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 4.dp),
                uiState = viewModel.uiState.collectAsState().value,
                uiEvent = viewModel::onEvent,
                navController = navController
            )
        }

        composable<HomeRoutes.ToShoppingList> {
            val args = it.toRoute<ToShoppingList>()
            ShoppingListScreenRoot(navController = navController, listId = args.id)
        }
    }
}