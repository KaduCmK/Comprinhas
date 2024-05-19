package com.example.comprinhas

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.comprinhas.data.ShoppingItem
import com.example.comprinhas.ui.home.HomeScreen
import com.example.comprinhas.ui.receipts.ReceiptsList
import com.example.comprinhas.ui.receipts.ReceiptsViewModel
import com.example.comprinhas.ui.settings.SettingsScreen
import com.example.comprinhas.ui.settings.SettingsViewModel
import com.example.comprinhas.ui.theme.ComprinhasTheme
import com.example.comprinhas.ui.welcome.WelcomeScreen
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

            ComprinhasTheme {
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        mainviewModel.getShoppingList()
                        HomeScreen(
                            viewModel = mainviewModel,
                            toWelcomeScreen = { navController.navigate("welcome") },
                            toSettingsScreen = { navController.navigate("settings") },
                            toReceiptsScreen = {
                                navController.navigate("receipts")
                                receiptsViewModel.getReceiptsList()
                            },
                            showDialog = { navController.navigate("addItem") }
                        )
                    }
                    composable("welcome") {
                        WelcomeScreen() { name, listId, listPassword, newList ->
                            settingsViewModel.updateUserPrefs(name, listId, listPassword)
                            if (newList) mainviewModel.createList(name, listId, listPassword)
                            navController.navigate("home")
                        }
                    }
                    composable("settings") {
                        SettingsScreen(
                            appPreferences = settingsViewModel.appPreferences,
                            updateUserPrefs = settingsViewModel::updateUserPrefs,
                            onNavigateBack = navController::popBackStack
                        )
                    }
                    composable("receipts") {
                        ReceiptsList(
                            receiptsFlow = receiptsViewModel.receiptsList,
                            onQrCodeScan = mainviewModel::scanQrCode,
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
        // TODO permissao de notificacao

        val name = "Adição e remoção de itens"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("list_notifications", name, importance)

        val notificationManager: NotificationManager =
            application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}