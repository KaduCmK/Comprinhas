package com.example.comprinhas.ui.receipts

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.comprinhas.ui.UiState
import com.example.comprinhas.ui.receipt.ReceiptBottomSheet
import com.example.comprinhas.ui.theme.ComprinhasTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun ReceiptsList(
    receiptsFlow: StateFlow<List<Receipt>>,
    onQrCodeScan: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    uiFlow: Flow<UiState>
) {
    val receiptsList by receiptsFlow.collectAsState(initial = emptyList())
    val uiState by uiFlow.collectAsState(initial = UiState.LOADING)
    var currentReceipt by remember { mutableStateOf<Receipt?>(null) }

    Scaffold(
        topBar = { ReceiptsTopBar(onQrCodeScan, onNavigateBack, uiState) }
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = paddingValues,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(receiptsList) {
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
        ReceiptsList(receiptsFlow = MutableStateFlow(emptyList()), uiFlow = emptyFlow())
    }
}