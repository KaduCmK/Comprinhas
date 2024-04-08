package com.example.comprinhas

import android.app.Application
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.Data
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

abstract class IMainViewModel()
    : ViewModel() {
    abstract var shoppingList: Flow<List<ShoppingItem>>
    abstract var cartList: Flow<List<ShoppingItem>>
    abstract val isLoading: Boolean
    abstract val appPreferences: AppPreferences

    abstract fun addShoppingList(item: ShoppingItem)
    abstract fun deleteShoppingItem(item: ShoppingItem)
    abstract fun moveToCart(item: ShoppingItem)
    abstract fun removeFromCart(item: ShoppingItem)
    abstract fun clearCart()
    abstract fun updateUserPrefs(name: String, listId: String)
    abstract fun updateNameAndListId(name: String, listId: String)

}

class ComprinhasViewModelPreview()
    : IMainViewModel() {
    override var shoppingList: Flow<List<ShoppingItem>>
        get() = flowOf(List(7) {ShoppingItem(name = "$it", addedBy = "Mock")})
        set(value) {}
    override var cartList: Flow<List<ShoppingItem>>
        get() = flowOf(List(3) {ShoppingItem(name = "$it", addedBy = "Mock")})
        set(value) {}
    override val isLoading: Boolean
        get() = false
    override val appPreferences: AppPreferences
        get() = AppPreferences(false, "Preview", "main", 123, false)

    override fun addShoppingList(item: ShoppingItem) {}

    override fun deleteShoppingItem(item: ShoppingItem) {}

    override fun moveToCart(item: ShoppingItem) {}

    override fun removeFromCart(item: ShoppingItem) {}

    override fun clearCart() {}

    override fun updateUserPrefs(name: String, listId: String) {}

    override fun updateNameAndListId(name: String, listId: String) {}


}

class ComprinhasViewModel(application: Application): IMainViewModel() {
    private val db = ShoppingListDatabase.getDatabase(application.applicationContext)
    private val dao = db.shoppingItemDao()
    private val repo = ShoppingListRepository(dao, application.baseContext)

    private val preferencesRepository =
        PreferencesRepository(application.dataStore, application.applicationContext)
    private val preferencesFlow =
        preferencesRepository.preferencesFlow

    private var _appPreferences by mutableStateOf(
        AppPreferences(false, "", "", 0, true)
    )
    override var shoppingList = repo.shoppingList
    override var cartList = repo.cartList

    override val isLoading
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

        val inputData = Data.Builder()
            .putString("name", runBlocking { appPreferences.name })
            .putString("listId", runBlocking { appPreferences.listId })
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

    override val appPreferences: AppPreferences
        get() {
            viewModelScope.launch {
                preferencesFlow.collect {
                    _appPreferences = AppPreferences(it.welcomeScreen, it.name, it.listId, it.lastChanged, it.isLoading)
                }
            }

            return _appPreferences
        }

    override fun addShoppingList(item: ShoppingItem) {
        viewModelScope.launch {
            preferencesRepository.updateLastChanged(item.id)
            repo.insert(item)
        }
    }

    override fun deleteShoppingItem(item: ShoppingItem) {
        viewModelScope.launch{
            val lastChanged = ZonedDateTime.now().toEpochSecond()
            preferencesRepository.updateLastChanged(lastChanged)
            repo.deleteFromList(item, lastChanged)
        }
    }

    override fun moveToCart(item: ShoppingItem) {
        viewModelScope.launch {
            repo.moveToCart(item.id)
        }

    }

    override fun removeFromCart(item: ShoppingItem) {
        viewModelScope.launch { repo.removeFromCart(item.id) }
    }
    override fun clearCart() {
        viewModelScope.launch {
            val lastChanged = ZonedDateTime.now().toEpochSecond()

            preferencesRepository.updateLastChanged(lastChanged)
            repo.clearCart(cartList.first().map { it.id }, lastChanged)
        }
    }

    override fun updateUserPrefs(name: String, listId: String) {
        viewModelScope.launch {
            preferencesRepository.updateWelcomeScreen(name, listId)
        }
    }

    override fun updateNameAndListId(name: String, listId: String) {
        viewModelScope.launch {
            preferencesRepository.updateNameAndListId(name, listId)
        }
    }
}