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
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.comprinhas.data.AppPreferences
import com.example.comprinhas.data.PreferencesRepository
import com.example.comprinhas.list.data.model.ShoppingItem
import com.example.comprinhas.core.data.db.ComprinhasDatabase
import com.example.comprinhas.home.data.ShoppingListRepository
import com.example.comprinhas.home.data.ShoppingList
import com.example.comprinhas.http.SyncWorker
import com.example.comprinhas.ui.UiState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

class ComprinhasViewModel(private val application: Application): AndroidViewModel(application) {
    private val db = ComprinhasDatabase.getDatabase(application.applicationContext)
    private val repo = ShoppingListRepository(
        db.shoppingItemDao(),
        db.shoppingListDao(),
        application.baseContext
    )

    private val preferencesRepository =
        PreferencesRepository(application.dataStore)
    private val preferencesFlow =
        preferencesRepository.preferencesFlow

    private var _appPreferences by mutableStateOf(
        AppPreferences(false, "", 0)
    )
    val appPreferences: AppPreferences
        get() {
            viewModelScope.launch {
                preferencesFlow.collect {
                    _appPreferences = AppPreferences(it.welcomeScreen, it.name, it.lastChanged)
                }
            }

            return _appPreferences
        }

    val welcomeScreen: Boolean
        get() {
            return runBlocking {
                preferencesFlow.first().welcomeScreen
            }
        }


    var shoppingLists = repo.shoppingLists
    var shoppingList = repo.shoppingList
    var cartList = repo.cartList

    var uiState = preferencesRepository.uiState

    fun createShopplingList(listName: String, listPassword: String) {
        viewModelScope.launch {
            preferencesRepository.updateUiState(UiState.LOADING)

            val res = repo.createShoppingList(appPreferences.name, listName, listPassword)
            Toast.makeText(application.baseContext, res, Toast.LENGTH_SHORT).show()

            preferencesRepository.updateUiState(UiState.LOADED)
        }
    }

    fun joinShoppingList(listName: String, listPassword: String) {
        viewModelScope.launch {
            preferencesRepository.updateUiState(UiState.LOADING)

            val res = repo.joinShoppingList(appPreferences.name, listName, listPassword)
            if (res.isNullOrEmpty()) {
                getShoppingList()
            }
            else {
                Toast.makeText(application.baseContext, res, Toast.LENGTH_SHORT).show()
            }

            preferencesRepository.updateUiState(UiState.LOADED)
        }
    }

    fun deleteList(listName: String, listPassword: String) {
        viewModelScope.launch {
            repo.deleteList(appPreferences.name, listName, listPassword)
        }
    }

    fun exitShoppingList(listName: String, listPassword: String) {
        viewModelScope.launch {
            repo.exitShoppingList(listName, listPassword)
        }
    }

    var currentList by mutableStateOf(ShoppingList(-1, "", "", ""))
    fun getCurrentList(listId: Int) {
        viewModelScope.launch {
            repo.shoppingLists.collect { list ->
                currentList = list.find { it.idLista == listId } ?: ShoppingList(-1, "", "", "")
                Log.d("VIEW-MODEL", "Mudando para lista ${currentList.idLista}/${currentList.nomeLista}")
            }
        }

    }

    fun getShoppingList() {
        viewModelScope.launch {
            preferencesRepository.updateUiState(UiState.LOADING)

            // TODO: sugestao de alterar check de internet p um listener constante

            val connectivityManager = application.getSystemService(ConnectivityManager::class.java)
            val networkCapabilities = connectivityManager.activeNetwork
            val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities)
            val hasInternet = actNw?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false

            if (!hasInternet) {
                preferencesRepository.updateUiState(UiState.NO_CONNECTION)
                Toast.makeText(
                    application.applicationContext,
                    "Sem conexão com a Internet",
                    Toast.LENGTH_SHORT)
                    .show()
            }

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val listId = repo.shoppingLists.stateIn(viewModelScope)
                .value
                .map { it.idLista }

            val inputData = Data.Builder()
                .putString("name", appPreferences.name)
                .putIntArray("idList", listId.toIntArray())
                .build()

            val periodicSync = PeriodicWorkRequest
                .Builder(SyncWorker::class.java, 20, TimeUnit.MINUTES)
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
    }

    fun addShoppingListItem(item: ShoppingItem) {
        viewModelScope.launch {
//            preferencesRepository.updateLastChanged(item.idItem)
            repo.insert(item)
        }
    }

    fun deleteShoppingItem(item: ShoppingItem) {
        viewModelScope.launch{
            val lastChanged = ZonedDateTime.now().toEpochSecond()
//            preferencesRepository.updateLastChanged(lastChanged)
            repo.deleteFromList(item, lastChanged)
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
    fun clearCart(listId: Int) {
        viewModelScope.launch {
            val lastChanged = ZonedDateTime.now().toEpochSecond()

//            preferencesRepository.updateLastChanged(lastChanged)
            repo.clearCart(cartList.first().map { it.idItem }, listId, lastChanged)
        }
    }
}