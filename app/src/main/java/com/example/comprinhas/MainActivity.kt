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
import com.example.comprinhas.data.ShoppingItem
import com.example.comprinhas.ui.UiState
import com.example.comprinhas.ui.home.HomeScreen
import com.example.comprinhas.ui.receipts.ReceiptsList
import com.example.comprinhas.ui.receipts.ReceiptsViewModel
import com.example.comprinhas.ui.settings.SettingsScreen
import com.example.comprinhas.ui.settings.SettingsViewModel
import com.example.comprinhas.ui.theme.ComprinhasTheme
import com.example.comprinhas.ui.welcome.SetListScreen
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
                    navigation(startDestination = "welcome/username", route = "welcome") {
                        composable("welcome/username") {
                            settingsViewModel.updateUiState(UiState.LOADED)

                            UsernameScreen { username, newList ->
                                navController.navigate(
                                    "welcome/setList/$username/$newList"
                                )
                            }
                        }

                        composable(
                            route = "welcome/setList/{username}/{newList}",
                            arguments = listOf(
                                navArgument("username") { type = NavType.StringType },
                                navArgument("newList") { type = NavType.BoolType }
                            ),
                            enterTransition = {
                                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
                            },
                            popExitTransition = {
                                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
                            }
                        ) {
                            val username = it.arguments?.getString("username") ?: ""
                            val newList = it.arguments?.getBoolean("newList") ?: false

                            SetListScreen(
                                uiState = uiState,
                                newList = newList,
                                login = settingsViewModel.login,
                                completeLogin = { navController.popBackStack("home", inclusive = false) }
                            ) {listName, listPassword ->
                                if (newList) {
                                    settingsViewModel.createList(username, listName, listPassword)
                                }
                                else {
                                    settingsViewModel.updateUserPrefs(username, listName, listPassword)
                                    navController.popBackStack("home", inclusive = false)
                                }

                            }
                        }
                    }

                    composable(
                        route = "home",
                        popEnterTransition = {
                            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End)
                        }
                    ) {
                        HomeScreen(
                            viewModel = mainviewModel,
                            uiState = uiState,
                            toWelcomeScreen = { navController.navigate("welcome") },
                            toSettingsScreen = { navController.navigate("settings") },
                            toReceiptsScreen = {
                                navController.navigate("receipts")
                                receiptsViewModel.getReceiptsList()
                            },
                            showDialog = { navController.navigate("addItem") }
                        )
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

                    dialog("addItem") {
                        InputDialog(
                            onDismiss = navController::popBackStack,
                            setValue = {
                                mainviewModel.addShoppingListItem(ShoppingItem(nomeItem = it, adicionadoPor = settingsViewModel.appPreferences.name))
                            })
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