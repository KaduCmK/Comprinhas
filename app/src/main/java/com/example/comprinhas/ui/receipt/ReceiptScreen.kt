package com.example.comprinhas.ui.receipt

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.VpnKey
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprinhas.data.Converters
import com.example.comprinhas.ui.receipts.Receipt
import com.example.comprinhas.ui.receipts.ReceiptItem
import com.example.comprinhas.ui.theme.ComprinhasTheme
import java.text.DecimalFormat

@Composable
fun ReceiptScreen(receipt: Receipt) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp, start = 24.dp, end = 24.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = receipt.nomeMercado,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = "CNPJ: ${Converters.parseCnpj(receipt.cnpjMercado)}",
            style = MaterialTheme.typography.titleSmall,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = Converters.isoInstantString_to_LocalDateTime(receipt.dataEmissao),
            style = MaterialTheme.typography.titleSmall,
        )
        Text(
            text = receipt.enderecoMercado,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider(
            color = MaterialTheme.colorScheme.primary,
            thickness = 2.dp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(0.7f),
                text = "Produto",
                style = MaterialTheme.typography.titleSmall)
            Text(
                modifier = Modifier.weight(0.12f),
                text = "Qtd",
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.weight(0.12f),
                text = "Valor",
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.weight(0.12f),
                text = "total",
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.End
            )
        }
        Divider(
            color = MaterialTheme.colorScheme.onBackground,
            thickness = 1.dp
        )
        LazyColumn(modifier = Modifier.weight(0.7f)) {
            items(receipt.itens) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        modifier = Modifier.weight(0.64f),
                        text = it.nomeproduto,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Start)
                    Text(
                        modifier = Modifier.weight(0.18f),
                        text = "${Converters.parseUnit(it.quantidade,  it.unidade)} ${it.unidade}",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center)
                    Text(
                        modifier = Modifier.weight(0.12f),
                        text = "${it.valor}",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center)
                    Text(
                        modifier = Modifier.weight(0.12f),
                        text = DecimalFormat("#0.00").format(
                            Converters.parseUnit(it.quantidade,  it.unidade)
                                .toFloat() * it.valor),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.End)
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(20.dp).padding(end = 4.dp),
                imageVector = Icons.Outlined.VpnKey,
                contentDescription = "chaveAcesso",
            )
            Text(text = receipt.chaveAcesso, style = MaterialTheme.typography.bodySmall)
        }
        Divider(
            color = MaterialTheme.colorScheme.primary,
            thickness = 2.dp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text(text = "Quantidade total: ")
            Text(text = "${receipt.itens.sumOf { it.quantidade }}")
        }
        Row {
            Text(text = "Valor total: ")
            Text(text = DecimalFormat("#.00").format(receipt.valorTotal))
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ReceiptScreenPreview() {
    ComprinhasTheme {
        Surface {
            ReceiptScreen(receipt = Receipt(
                itens = List(3) {
                    ReceiptItem("Produto $it", 2, "UN", 9.80f)
                }
            ))
        }
    }
}