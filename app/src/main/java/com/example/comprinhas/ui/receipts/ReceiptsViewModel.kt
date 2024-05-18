package com.example.comprinhas.ui.receipts

import android.app.Application
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.comprinhas.dataStore
import com.example.comprinhas.http.DatabaseApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ReceiptsViewModel(private val application: Application) : AndroidViewModel(application) {
    private val retrofitService = DatabaseApi.retrofitService

    private var _receiptsList = MutableStateFlow<List<Receipt>>(emptyList())
    val receiptsList: StateFlow<List<Receipt>>
        get() = _receiptsList



    fun getReceiptsList() {
        viewModelScope.launch {
            val username = runBlocking { application.applicationContext.dataStore.data.map {
                it[stringPreferencesKey("user_name")] ?: ""
            } }.first()

            Log.d("ReceiptsViewModel", "getReceiptsList: $username")

            _receiptsList.value = retrofitService.getReceiptsByUsername(username).body()!!
        }
    }
}