package com.example.comprinhas.ui.receipts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.comprinhas.ui.theme.ComprinhasTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ReceiptsList(
    receiptsFlow: StateFlow<List<Receipt>>,
    onQrCodeScan: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val receiptsList by receiptsFlow.collectAsState(initial = emptyList())

    Scaffold(
        topBar = { ReceiptsTopBar(onQrCodeScan, onNavigateBack) }
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = paddingValues,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(receiptsList) {
                ReceiptCard(receipt = it, onOpenReceipt = {})
            }
        }
    }
}

@Preview
@Composable
private fun ReceiptsScreenPreview() {
    ComprinhasTheme {
        ReceiptsList(receiptsFlow = MutableStateFlow(emptyList()))
    }
}