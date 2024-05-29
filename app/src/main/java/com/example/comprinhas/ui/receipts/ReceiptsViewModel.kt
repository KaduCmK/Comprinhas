package com.example.comprinhas.ui.receipts

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.comprinhas.data.PreferencesRepository
import com.example.comprinhas.dataStore
import com.example.comprinhas.http.BodyRequest
import com.example.comprinhas.http.DatabaseApi
import com.example.comprinhas.http.ReceiptWorker
import com.example.comprinhas.ui.UiState
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant

class ReceiptsViewModel(private val application: Application) : AndroidViewModel(application) {
    private val retrofitService = DatabaseApi.retrofitService

    private val username = runBlocking {
        application.baseContext.dataStore.data.map {
            it[stringPreferencesKey("user_name")] ?: ""
        }.first()
    }

    private var _receiptsList = MutableStateFlow<List<Receipt>>(emptyList())
    val receiptsList: StateFlow<List<Receipt>>
        get() = _receiptsList

    private var _uiState = MutableStateFlow(UiState.LOADING)
    val uiState: StateFlow<UiState>
        get() = _uiState


    fun getReceiptsList() {
        viewModelScope.launch {
            _uiState.value = UiState.LOADING
            val response = retrofitService.getReceiptsByUsername(username)

            Log.d("ReceiptsViewModel", "getReceiptsList: $username")

            if (response.code() == 200) {
                _receiptsList.value = response.body()!!
            } else {
                Toast.makeText(
                    application.baseContext,
                    "Erro ao recuperar notas",
                    Toast.LENGTH_SHORT
                ).show()
            }
            _uiState.value = UiState.LOADED
        }
    }

    fun scanQrCode() {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()

        val scanner = GmsBarcodeScanning.getClient(getApplication(), options)

        scanner.startScan()
            .addOnSuccessListener {
                _uiState.value = UiState.LOADING
                Toast.makeText(getApplication(), "Enviando recibo...", Toast.LENGTH_SHORT).show()

                val inputData = Data.Builder().apply {
                    putString("username", username)
                    putString("url", it.rawValue)
                }.build()

                val receiptWorkRequest = OneTimeWorkRequest.Builder(ReceiptWorker::class.java)
                    .setInputData(inputData)
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED).build()
                    )
                    .build()

                val wm = WorkManager.getInstance(application.baseContext)
                val workerId = "receipt-${Instant.now().epochSecond}"
                wm.enqueueUniqueWork(
                    workerId,
                    ExistingWorkPolicy.APPEND_OR_REPLACE,
                    receiptWorkRequest
                )

                viewModelScope.launch {
                    _uiState.value = UiState.LOADING

                    wm.getWorkInfosForUniqueWorkFlow(workerId)
                        .collect {
                            Log.d(("ViewModel-QRCODE"), it.toString())

                            if (it[0].state == WorkInfo.State.SUCCEEDED) {
                                _uiState.value = UiState.LOADED
                                Toast.makeText(
                                    application.baseContext,
                                    "Recibo enviado com sucesso",
                                    Toast.LENGTH_SHORT
                                ).show()

                                getReceiptsList()
                            } else if (it[0].state == WorkInfo.State.FAILED) {
                                _uiState.value = UiState.LOADED
                                Toast.makeText(
                                    application.baseContext,
                                    "Erro ao enviar recibo",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(getApplication(), "Falha no leitor de QR Code", Toast.LENGTH_SHORT)
                    .show()
                Log.d("ViewModel-QRCODE", it.message.toString())
            }
    }
}