package io.github.kaducmk.comprinhas.receipts_list.presentation

import android.app.Application
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import io.github.kaducmk.comprinhas.dataStore
import io.github.kaducmk.comprinhas.receipts_list.data.model.Receipt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class ReceiptsViewModel(private val application: Application) : AndroidViewModel(application) {
//    private val retrofitService = DatabaseApi.retrofitService

    private val username: String
        get() = runBlocking {
            application.baseContext.dataStore.data.map {
                it[stringPreferencesKey("user_name")] ?: ""
            }.first()
        }

    private var _receiptsList = MutableStateFlow<List<Receipt>>(emptyList())
    val receiptsList: StateFlow<List<Receipt>>
        get() = _receiptsList

//    private var _uiState = MutableStateFlow(UiState.LOADING)
//    val uiState: StateFlow<UiState>
//        get() = _uiState


//    fun getReceiptsList() {
//        viewModelScope.launch {
//            _uiState.value = UiState.LOADING
////            val response = retrofitService.getReceiptsByUsername(username)
//
//            Log.d("ReceiptsViewModel", "getReceiptsList: $username")
//
//            if (response.code() == 200) {
//                _receiptsList.value = response.body()!!
//            } else {
//                Toast.makeText(
//                    application.baseContext,
//                    "Erro ao recuperar notas",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            _uiState.value = UiState.LOADED
//        }
//    }
}