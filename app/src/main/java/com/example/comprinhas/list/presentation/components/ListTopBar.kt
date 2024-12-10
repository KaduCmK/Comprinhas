package com.example.comprinhas.list.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprinhas.core.data.model.Usuario
import com.example.comprinhas.list.data.model.ShoppingListUiState
import com.example.comprinhas.list.presentation.dialogs.QrCodeDialog
import com.example.comprinhas.ui.theme.ComprinhasTheme

@Composable
fun ListTopBar(
    modifier: Modifier = Modifier,
    uiState: ShoppingListUiState,
    onAddItem: () -> Unit,
    onShowQrCode: (Boolean) -> Unit
) {
    ((uiState as? ShoppingListUiState.Loaded)?.qrCode)?.let {
        QrCodeDialog(bitmap = it, onDismiss = { onShowQrCode(false) })
    }

    Surface {
        Column(
            modifier = modifier.padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Lista de Compras",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = uiState.shoppingList?.nome ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic
                    )
                }
                IconButton(onClick = { onShowQrCode(true) }) {
                    Icon(imageVector = Icons.Default.QrCode, contentDescription = "Options")
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    enabled = uiState is ShoppingListUiState.Loaded,
                    onClick = { onAddItem() }
                ) {
                    Text(
                        style = MaterialTheme.typography.titleMedium,
                        text = "Adicionar"
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (uiState is ShoppingListUiState.Loading)
                LinearProgressIndicator()
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ListTopBarPreview() {
    ComprinhasTheme {
        ListTopBar(
            uiState = ShoppingListUiState.Loading(
                currentUser = null,
                com.example.comprinhas.home.data.model.ShoppingList(
                    id = "1",
                    nome = "Daiso",
                    senha = "",
                    criador = Usuario(
                        uid = "",
                        displayName = "Lucas",
                        email = "",
                        photoUrl = ""
                    ),
                    participantes = emptyList()
                ),
                emptyList(),
                emptyList()
            ),
            onAddItem = {},
            onShowQrCode = {}
        )
    }
}