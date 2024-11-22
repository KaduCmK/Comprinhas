package com.example.comprinhas.receipts_list.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprinhas.core.data.Converters
import com.example.comprinhas.receipts_list.data.model.Receipt
import com.example.comprinhas.ui.theme.ComprinhasTheme
import java.text.DecimalFormat

@Composable
fun ReceiptCard(
    modifier: Modifier = Modifier,
    receipt: Receipt,
    onOpenReceipt: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onOpenReceipt() }
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = receipt.nomeMercado,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Outlined.CalendarMonth, contentDescription = "Data")
                    Text(
                        text = Converters.isoInstantString_to_ExtendedDate(receipt.dataEmissao),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.width(24.dp))
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Money, contentDescription = "custo")
                    Text(
                        text = "R$ ${DecimalFormat("0.00").format(receipt.valorTotal)}",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ReceiptCardPreview() {
    ComprinhasTheme {
        ReceiptCard(onOpenReceipt = {},
            receipt = Receipt()
        )
    }
}