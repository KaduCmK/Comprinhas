package com.example.comprinhas.receipt.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import com.example.comprinhas.receipt.presentation.ReceiptScreen
import com.example.comprinhas.receipts_list.data.model.Receipt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptBottomSheet(
    receipt: Receipt,
    onDismiss: () -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState
        ) {
        ReceiptScreen(receipt)
    }
}