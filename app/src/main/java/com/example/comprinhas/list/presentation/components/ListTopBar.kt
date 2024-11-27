package com.example.comprinhas.list.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.CloudOff
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comprinhas.core.data.model.Usuario
import com.example.comprinhas.home.data.model.HomeUiState
import com.example.comprinhas.list.data.model.ShoppingListUiState
import com.example.comprinhas.ui.theme.ComprinhasTheme

@Composable
fun ListTopBar(modifier: Modifier = Modifier, uiState: ShoppingListUiState, onAddItem: () -> Unit) {
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
//                IconButton(onClick = {}) {
//                    Icon(
//                        modifier = Modifier.size(32.dp),
//                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                        contentDescription = "Back Button"
//                    )
//                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Lista de Compras", style = TextStyle(
                            fontSize = 24.sp, fontWeight = FontWeight(700)
                        )
                    )
                    Text(
                        text = uiState.shoppingList?.nome ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
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

                IconButton(
                    onClick = {},
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ReceiptLong,
                        contentDescription = null
                    )
                }
            }
//            if (uiState is ShoppingListUiState.NoInternet) {
//            Row(
//                modifier = Modifier.padding(top = 16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    modifier = Modifier.padding(horizontal = 16.dp),
//                    imageVector = Icons.Outlined.CloudOff,
//                    contentDescription = "No Connection"
//                )
//                Text(
//                    text = "Sem conexão com o servidor",
//                    style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic)
//                )
//            }
//            }
//            else if (uiState is HomeUiState.Loading) {
            Spacer(modifier = Modifier.height(16.dp))
//            LinearProgressIndicator()
//            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ListTopBarPreview() {
    ComprinhasTheme {
        ListTopBar(
            uiState = ShoppingListUiState.Loading(
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
                )
            ),
            onAddItem = {}
        )
    }
}