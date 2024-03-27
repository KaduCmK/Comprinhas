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
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.comprinhas.data.AppPreferences
import com.example.comprinhas.data.PreferencesRepository
import com.example.comprinhas.data.ShoppingItem
import com.example.comprinhas.data.ShoppingListDatabase
import com.example.comprinhas.data.ShoppingListRepository
import com.example.comprinhas.http.SyncWorker
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

//fun mockShoppingList() = List(7) { ShoppingItem("Compra $it", "Mock")}

class ComprinhasViewModel(application: Application): AndroidViewModel(application) {
    private val db = ShoppingListDatabase.getDatabase(application.applicationContext)
    private val dao = db.shoppingItemDao()
    private val repo = ShoppingListRepository(dao, application.baseContext)

    private val preferencesRepository =
        PreferencesRepository(application.dataStore, application.applicationContext)
    private val preferencesFlow =
        preferencesRepository.preferencesFlow

    private var _appPreferences by mutableStateOf(
        AppPreferences("", false, 0, true)
    )
    var shoppingList = repo.shoppingList
    var cartList = repo.cartList

    val isLoading
        get() = _appPreferences.isLoading

    init {
        val connectivityManager = application.getSystemService(ConnectivityManager::class.java)
        val networkCapabilities = connectivityManager.activeNetwork
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities)
        val hasInternet = actNw?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false

        runBlocking { preferencesRepository.updateLoadingScreen(true) }

        if (!hasInternet) {
            runBlocking { preferencesRepository.updateLoadingScreen(false) }
            Toast.makeText(
                application.applicationContext,
                "Sem conexão com a Internet",
                Toast.LENGTH_SHORT)
                .show()
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicSync = PeriodicWorkRequest
            .Builder(SyncWorker::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        val workManager = WorkManager.getInstance(application.applicationContext)

        workManager
            .enqueueUniquePeriodicWork(
                "periodicSync",
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                periodicSync)
    }

    val appPreferences: AppPreferences
        get() {
            Log.d("VIEW_MODEL", "lastChanged = ${_appPreferences.lastChanged}")

            viewModelScope.launch {
                preferencesFlow.collect {
                    _appPreferences = AppPreferences(it.name, it.welcomeScreen, it.lastChanged, it.isLoading)
                }
            }

            return _appPreferences
        }

    fun addShoppingList(item: ShoppingItem) {
        viewModelScope.launch {
            preferencesRepository.updateLastChanged(item.id)
            repo.insert(item)
        }
    }

    fun deleteShoppingItem(item: ShoppingItem) {
        viewModelScope.launch{
            val lastChanged = ZonedDateTime.now().toEpochSecond()
            preferencesRepository.updateLastChanged(lastChanged)
            repo.deleteFromList(item, lastChanged)
        }
    }

    fun moveToCart(item: ShoppingItem) {
        viewModelScope.launch {
            repo.moveToCart(item.id)
        }

    }

    fun removeFromCart(item: ShoppingItem) {
        viewModelScope.launch { repo.removeFromCart(item.id) }
    }
    fun clearCart() {
        viewModelScope.launch {
            val lastChanged = ZonedDateTime.now().toEpochSecond()

            preferencesRepository.updateLastChanged(lastChanged)
            repo.clearCart(cartList.first().map { it.id }, lastChanged)
        }
    }

    fun updateUserPrefs(name: String) {
        viewModelScope.launch {
            preferencesRepository.updateNameAndWelcomeScreen(name, false)
        }
    }
}