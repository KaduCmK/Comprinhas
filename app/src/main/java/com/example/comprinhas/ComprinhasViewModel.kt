package com.example.comprinhas

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.comprinhas.http.DatabaseApi
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.comprinhas.data.AppPreferences
import com.example.comprinhas.data.PreferencesRepository
import com.example.comprinhas.data.ShoppingItem
import com.example.comprinhas.data.ShoppingListDatabase
import com.example.comprinhas.data.ShoppingListRepository
import com.example.comprinhas.http.SyncWorker
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

//fun mockShoppingList() = List(7) { ShoppingItem("Compra $it", "Mock")}

class ComprinhasViewModel(application: Application): AndroidViewModel(application) {
    private val db = ShoppingListDatabase.getDatabase(application.applicationContext)
    private val dao = db.shoppingItemDao()
    private val repo = ShoppingListRepository(dao, application.baseContext)

    private val hasInternet = false
    private val pendingTasks: MutableList<WorkRequest> = mutableListOf()

    private val preferencesRepository =
        PreferencesRepository(application.dataStore, application.applicationContext)
    private val preferencesFlow =
        preferencesRepository.preferencesFlow

    private var _appPreferences by mutableStateOf(AppPreferences("", false, -1))
    var shoppingList = repo.shoppingList
    var cartList = repo.cartList

    init {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val inputData = Data.Builder()
            .putLong("localLastChanged", _appPreferences.lastChanged)
            .build()

        val periodicSync = PeriodicWorkRequest
           .Builder(SyncWorker::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(application.applicationContext)
            .enqueueUniquePeriodicWork(
                "periodicSync",
                ExistingPeriodicWorkPolicy.KEEP,
                periodicSync)
    }

    val appPreferences: AppPreferences
        get() {
            viewModelScope.launch {
                preferencesFlow.collect {
                    _appPreferences = AppPreferences(it.name, it.welcomeScreen, it.lastChanged)
                }
            }

            return _appPreferences
        }


    fun addShoppingList(item: ShoppingItem) {
        viewModelScope.launch {
            repo.insert(item, internet = true)
        }
    }

    fun deleteShoppingItem(item: ShoppingItem) {
        viewModelScope.launch{
            repo.deleteFromList(item, internet = true)
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

            var working = true
            cartList.takeWhile { working }.collect { shoppingItems ->
                DatabaseApi.retrofitService.clearCart(shoppingItems.map { it.id }
                    .also {
                        working = false
                        repo.clearCart()
                    })
            }
        }
    }

    fun updateUserPrefs(name: String) {
        viewModelScope.launch {
            preferencesRepository.updateNameAndWelcomeScreen(name, false)
        }
    }
}