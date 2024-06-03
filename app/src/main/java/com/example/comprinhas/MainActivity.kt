package com.example.comprinhas

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.comprinhas.data.shoppingItem.ShoppingItem
import com.example.comprinhas.ui.UiState
import com.example.comprinhas.ui.home.EditListDialog
import com.example.comprinhas.ui.home.HomeScreen
import com.example.comprinhas.ui.home.NewListDialog
import com.example.comprinhas.ui.list.ShoppingListScreen
import com.example.comprinhas.ui.receipts.ReceiptsList
import com.example.comprinhas.ui.receipts.ReceiptsViewModel
import com.example.comprinhas.ui.settings.SettingsScreen
import com.example.comprinhas.ui.settings.SettingsViewModel
import com.example.comprinhas.ui.theme.ComprinhasTheme
import com.example.comprinhas.ui.welcome.UsernameScreen
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    companion object {
        var isForeground = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isForeground = true

        qrCodeConfiguration()
        notificationsConfiguration()

        setContent {
            val navController = rememberNavController()

            val mainviewModel: ComprinhasViewModel = viewModel()
            val settingsViewModel: SettingsViewModel = viewModel()
            val receiptsViewModel: ReceiptsViewModel = viewModel()

            val uiState by mainviewModel.uiState.collectAsState(initial = UiState.LOADED)

            ComprinhasTheme {
                NavHost(navController = navController, startDestination = "home") {
                    navigation(
                        startDestination = "welcome/username",
                        route = "welcome"
                    ) {
                        composable("welcome/username") {
                            UsernameScreen { username ->
                                settingsViewModel.updateUserPrefs(username)
                                navController.popBackStack("home", inclusive = false)
                            }
                        }
                    }

                    composable(
                        route = "home",
                        popEnterTransition = {
                            EnterTransition.None
                        }
                    ) {
                        val shoppingLists by
                            mainviewModel.shoppingLists.collectAsState(initial = emptyList())

                        HomeScreen(
                            shoppingLists = shoppingLists,
                            welcome = mainviewModel.welcomeScreen,
                            toWelcomeScreen = { navController.navigate("welcome") },
                            getShoppingItems = mainviewModel::getShoppingList,
                            onJoinList = { navController.navigate("joinList/$it") },
                            onNavigateToList = { navController.navigate("shoppingList/$it") },
                            onHoldList = {
                                         navController.navigate("editList/${it.nomeLista}/${it.senhaLista}")
                            },
                            uiState = uiState
                        )
                    }
                    dialog(
                        route = "joinList/{newList}",
                        arguments = listOf(navArgument("newList") { type = NavType.BoolType })
                    ) {
                        val newList = it.arguments?.getBoolean("newList") ?: false

                        NewListDialog(
                            onDismiss = navController::popBackStack,
                            newList = newList
                        ) { listName, listPassword ->
                            if (newList) mainviewModel.createShopplingList(listName, listPassword)
                            else mainviewModel.joinShoppingList(listName, listPassword)
                        }
                    }
                    dialog(
                        route = "editList/{listName}/{listPassword}",
                        arguments = listOf(
                            navArgument("listName") { type = NavType.StringType },
                            navArgument("listPassword") { type = NavType.StringType }
                        )
                    ) {
                        val listName = it.arguments?.getString("listName") ?: ""
                        val listPassword = it.arguments?.getString("listPassword") ?: ""

                        EditListDialog(
                            onDismissRequest = navController::popBackStack,
                            listName = listName,
                            onDeleteList = {
                                mainviewModel.deleteList(listName, listPassword)
                                navController.popBackStack()
                            },
                            onEditList = {},
                            onExitList = {
                                mainviewModel.exitShoppingList(listName, listPassword)
                            }
                        )
                    }

                    navigation("shoppingList/{listId}", route = "shoppingList") {
                        composable(
                            route = "shoppingList/{listId}",
                            arguments = listOf(navArgument("listId") { type = NavType.IntType }),
                            enterTransition = { expandVertically() },
                            popExitTransition = { shrinkVertically() }
                        ) {
                            val listId = it.arguments?.getInt("listId") ?: -1

                            ShoppingListScreen(
                                viewModel = mainviewModel,
                                uiState = uiState,
                                listId = listId,
                                toWelcomeScreen = { navController.navigate("welcome") },
                                toSettingsScreen = { navController.navigate("settings") },
                                toReceiptsScreen = {
                                    navController.navigate("receipts")
                                    receiptsViewModel.getReceiptsList()
                                },
                                showDialog = { navController.navigate("shoppingList/addItem/$listId") }
                            )
                        }
                        dialog(
                            route = "shoppingList/addItem/{listId}",
                            arguments = listOf(navArgument("listId") { type = NavType.IntType }),
                        ) {
                            InputDialog(
                                onDismiss = navController::popBackStack,
                                setValue = {nome ->
                                    mainviewModel.addShoppingListItem(
                                        ShoppingItem(
                                            nomeItem = nome,
                                            adicionadoPor = settingsViewModel.appPreferences.name,
                                            idLista = it.arguments?.getInt("listId") ?: -1
                                        ),
                                    )
                                })
                        }
                    }
                    composable(
                        route = "settings",
                        enterTransition = {
                            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
                        },
                        popExitTransition = {
                            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
                        }
                    ) {
                        SettingsScreen(
                            appPreferences = settingsViewModel.appPreferences,
                            updateUserPrefs = settingsViewModel::updateUserPrefs,
                            onNavigateBack = navController::popBackStack
                        )
                    }
                    composable(
                        route = "receipts",
                        enterTransition = {
                            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
                        },
                        popExitTransition = {
                            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
                        }
                    ) {
                        ReceiptsList(
                            receiptsFlow = receiptsViewModel.receiptsList,
                            onQrCodeScan = receiptsViewModel::scanQrCode,
                            uiState = uiState,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        isForeground = false
    }

    private fun qrCodeConfiguration() {
        val moduleInstallCLient = ModuleInstall.getClient(this)
        val optionalModuleApi = GmsBarcodeScanning.getClient(this)

        moduleInstallCLient
            .areModulesAvailable(optionalModuleApi)
            .addOnSuccessListener {
                Toast.makeText(this, "Módulos disponíveis", Toast.LENGTH_SHORT).show()

                if (!it.areModulesAvailable()) {
                    Toast.makeText(this, "QRCode não está presente", Toast.LENGTH_SHORT).show()

                    val moduleInstallRequest = ModuleInstallRequest.newBuilder()
                        .addApi(optionalModuleApi)
                        .build()

                    moduleInstallCLient.installModules(moduleInstallRequest)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Módulo instalado", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Falha ao instalar módulo", Toast.LENGTH_SHORT)
                                .show()
                        }
                } else {
                    Toast.makeText(this, "QRCode está presente", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                // TODO: tratar falha na obtencao do leitor de qr code
            }
    }

    private fun notificationsConfiguration() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
        // TODO: separação para melhor UX

        val name = "Adição e remoção de itens"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("list_notifications", name, importance)

        val notificationManager: NotificationManager =
            application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}