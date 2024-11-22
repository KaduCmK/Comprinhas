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
import com.example.comprinhas.auth.data.AuthService
import com.example.comprinhas.list.data.model.ShoppingItem
import com.example.comprinhas.ui.UiState
import com.example.comprinhas.home.presentation.dialogs.EditListDialog
import com.example.comprinhas.home.presentation.HomeScreen
import com.example.comprinhas.home.presentation.dialogs.NewListDialog
import com.example.comprinhas.list.presentation.ShoppingListScreen
import com.example.comprinhas.receipts_list.presentation.ReceiptsList
import com.example.comprinhas.receipts_list.presentation.ReceiptsViewModel
import com.example.comprinhas.settings.presentation.SettingsScreen
import com.example.comprinhas.settings.presentation.SettingsViewModel
import com.example.comprinhas.ui.theme.ComprinhasTheme
import com.example.comprinhas.auth.presentation.AuthScreen
import com.example.comprinhas.list.presentation.dialogs.InputDialog
import com.example.comprinhas.ui.navigation.MainNavGraph
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var authService: AuthService

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

            ComprinhasTheme {
                MainNavGraph(navController, authService)
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