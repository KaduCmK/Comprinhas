package com.example.comprinhas.ui.receipt

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.comprinhas.ui.receipts.Receipt
import com.example.comprinhas.ui.receipts.ReceiptItem
import com.example.comprinhas.ui.theme.ComprinhasTheme

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