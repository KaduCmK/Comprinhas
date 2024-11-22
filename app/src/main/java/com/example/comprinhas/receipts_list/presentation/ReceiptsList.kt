package com.example.comprinhas.receipts_list.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprinhas.core.presentation.TopBar
import com.example.comprinhas.ui.UiState
import com.example.comprinhas.receipt.presentation.components.ReceiptBottomSheet
import com.example.comprinhas.receipts_list.data.model.Receipt
import com.example.comprinhas.receipts_list.presentation.components.ReceiptCard
import com.example.comprinhas.ui.theme.ComprinhasTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// TODO: tratar acesso a lista quando nao há internet

@Composable
fun ReceiptsList(
    receiptsFlow: StateFlow<List<Receipt>>,
    onQrCodeScan: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    uiState: UiState = UiState.LOADED
) {
    val receiptsList by receiptsFlow.collectAsState(initial = emptyList())
    var currentReceipt by remember { mutableStateOf<Receipt?>(null) }

    Scaffold(
        topBar = {
            TopBar(
                title = "Notas Fiscais",
                backButton = onNavigateBack,
                mainButton = {
                    Button(onClick = onQrCodeScan) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.QrCodeScanner,
                                contentDescription = "Escanear QR Code"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Escanear QR Code")
                        }
                    }
                },
                uiState = uiState
            )
        }
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = paddingValues,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(key = { it.chaveAcesso }, items = receiptsList) {
                ReceiptCard(receipt = it, onOpenReceipt = { currentReceipt = it })
            }
        }

        if (currentReceipt != null) {
            ReceiptBottomSheet(
                receipt = currentReceipt!!,
                onDismiss = { currentReceipt = null }
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ReceiptsScreenPreview() {
    ComprinhasTheme {
        ReceiptsList(receiptsFlow = MutableStateFlow(emptyList()))
    }
}