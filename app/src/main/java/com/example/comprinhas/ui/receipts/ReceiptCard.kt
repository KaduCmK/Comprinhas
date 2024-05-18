package com.example.comprinhas.ui.receipts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprinhas.ui.theme.ComprinhasTheme

@Composable
fun ReceiptCard(
    modifier: Modifier = Modifier,
    receipt: Receipt,
    onOpenReceipt: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onOpenReceipt() }
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = receipt.nomeMercado,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Row {
                    Icon(imageVector = Icons.Outlined.CalendarMonth, contentDescription = "Data")
                    Text(
                        text = receipt.dataEmissao,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.width(24.dp))
                Row{
                    Icon(imageVector = Icons.Default.AttachMoney, contentDescription = "custo")
                    Text(text = "R$ ${receipt.valorTotal}")
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
            receipt = Receipt("Kadu",
                "17/05/2023",
                "Supermercado",
                12.68f,
                emptyList())
        )
    }
}