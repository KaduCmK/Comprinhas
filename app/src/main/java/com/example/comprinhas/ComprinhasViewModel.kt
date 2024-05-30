package com.example.comprinhas

import android.app.Application
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.comprinhas.data.AppPreferences
import com.example.comprinhas.data.PreferencesRepository
import com.example.comprinhas.data.ShoppingItem
import com.example.comprinhas.data.ShoppingListDatabase
import com.example.comprinhas.data.ShoppingListRepository
import com.example.comprinhas.http.ReceiptWorker
import com.example.comprinhas.http.SyncWorker
import com.example.comprinhas.ui.UiState
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

class ComprinhasViewModel(private val application: Application): AndroidViewModel(application) {
    private val db = ShoppingListDatabase.getDatabase(application.applicationContext)
    private val dao = db.shoppingItemDao()
    private val repo = ShoppingListRepository(dao, application.baseContext)

    private val preferencesRepository =
        PreferencesRepository(application.dataStore, application.applicationContext)
    private val preferencesFlow =
        preferencesRepository.preferencesFlow

    private var _appPreferences by mutableStateOf(
        AppPreferences(false, "", "", "", 0)
    )
    val appPreferences: AppPreferences
        get() {
            viewModelScope.launch {
                preferencesFlow.collect {
                    _appPreferences = AppPreferences(it.welcomeScreen, it.name, it.listId, it.listPassword, it.lastChanged)
                }
            }

            return _appPreferences
        }

    var shoppingList = repo.shoppingList
    var cartList = repo.cartList

    var uiState = preferencesRepository.uiState

    fun getShoppingList() {
        runBlocking { preferencesRepository.updateUiState(UiState.LOADING) }

        val connectivityManager = application.getSystemService(ConnectivityManager::class.java)
        val networkCapabilities = connectivityManager.activeNetwork
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities)
        val hasInternet = actNw?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false

        if (!hasInternet) {
            runBlocking { preferencesRepository.updateUiState(UiState.NO_INTERNET) }
            Toast.makeText(
                application.applicationContext,
                "Sem conexão com a Internet",
                Toast.LENGTH_SHORT)
                .show()
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val inputData = Data.Builder()
            .putString("name", runBlocking { appPreferences.name })
            .putString("listId", runBlocking { appPreferences.listId })
            .putString("listPassword", runBlocking { appPreferences.listPassword })
            .build()

        val periodicSync = PeriodicWorkRequest
            .Builder(SyncWorker::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()

        val workManager = WorkManager.getInstance(application.applicationContext)

        workManager
            .enqueueUniquePeriodicWork(
                "periodicSync",
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                periodicSync)
    }

    fun createList(username: String, listName: String, listPassword: String): Boolean {
        return runBlocking {
            preferencesRepository.updateUiState(UiState.LOADING)
            val response = repo.crateList(username, listName, listPassword)

            if (response == 200) {
                preferencesRepository.updateUiState(UiState.LOADED)
                preferencesRepository.updateUserPrefs(username, listName, listPassword)
                true
            }
            else {
                preferencesRepository.updateUiState(UiState.NO_INTERNET)
                false
            }
        }
    }

    fun addShoppingListItem(item: ShoppingItem) {
        viewModelScope.launch {
//            preferencesRepository.updateLastChanged(item.idItem)
            repo.insert(item, appPreferences.listId)
        }
    }

    fun deleteShoppingItem(item: ShoppingItem) {
        viewModelScope.launch{
            val lastChanged = ZonedDateTime.now().toEpochSecond()
//            preferencesRepository.updateLastChanged(lastChanged)
            repo.deleteFromList(item, appPreferences.listId, lastChanged)
        }
    }

    fun moveToCart(item: ShoppingItem) {
        viewModelScope.launch {
            repo.moveToCart(item.idItem)
        }

    }

    fun removeFromCart(item: ShoppingItem) {
        viewModelScope.launch { repo.removeFromCart(item.idItem) }
    }
    fun clearCart() {
        viewModelScope.launch {
            val lastChanged = ZonedDateTime.now().toEpochSecond()

//            preferencesRepository.updateLastChanged(lastChanged)
            repo.clearCart(cartList.first().map { it.idItem }, appPreferences.listId, lastChanged)
        }
    }
}