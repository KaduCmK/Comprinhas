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
import com.example.comprinhas.ui.settings.SettingsScreen
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
            val viewModel: ComprinhasViewModel = viewModel()
            val appPreferences = viewModel.appPreferences

            ComprinhasTheme {
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            viewModel = viewModel,
                            toWelcomeScreen = { navController.navigate("welcome") },
                            toSettingsScreen = { navController.navigate("settings") },
                            showDialog = { navController.navigate("addItem") }
                        )
                    }
                    composable("welcome") {
                        WelcomeScreen() { name, listId ->
                            viewModel.updateUserPrefs(name, listId)
                            navController.navigate("home")
                        }
                    }
                    composable("settings") {
                        SettingsScreen(
                            appPreferences = appPreferences,
                            updateNameAndListId = viewModel::updateNameAndListId,
                            onNavigateBack = navController::popBackStack
                        )
                    }
                    dialog("addItem") {
                        InputDialog(
                            onDismiss = navController::popBackStack,
                            setValue = {
                                viewModel.addShoppingList(ShoppingItem(nomeItem = it, adicionadoPor = appPreferences.name))
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
        val name = "Adição e remoção de itens"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("list_notifications", name, importance)

        val notificationManager: NotificationManager =
            application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}