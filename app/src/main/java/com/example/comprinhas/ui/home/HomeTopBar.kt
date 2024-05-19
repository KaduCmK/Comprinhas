package com.example.comprinhas.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comprinhas.ui.UiState
import com.example.comprinhas.ui.theme.ComprinhasTheme

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    showDialog: () -> Unit,
    toSettings: () -> Unit,
    toReceiptsScreen: () -> Unit,
    uiState: UiState = UiState.LOADED
) {
    Column(
        modifier = modifier
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LocalContext.current

            Text(
                modifier = modifier.weight(1f),
                text = "Lista de Compras",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight(700)
                )
            )
            IconButton(
                onClick = toSettings,
            )  {
                Icon(
                    modifier = Modifier.size(35.dp),
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings")
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Button(
                modifier = modifier,
                enabled = uiState == UiState.LOADED,
                onClick = showDialog
            ) {
                Text(

                    style = MaterialTheme.typography.titleMedium,
                    text = "Adicionar"
                )
            }
            IconButton(
                onClick = toReceiptsScreen,
            )  {
                Icon(imageVector = Icons.AutoMirrored.Outlined.ReceiptLong, contentDescription = null)
            }
        }

        if (uiState == UiState.NO_INTERNET) {
            Row(
                modifier = Modifier.padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    imageVector = Icons.Outlined.CloudOff,
                    contentDescription = "No Connection")
                Text(
                    text = "Sem conexão com a internet",
                    style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic))
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
private fun TopBarPreview() {
    ComprinhasTheme {
        Surface {
            TopBar(
                showDialog = {},
                toSettings = {},
                toReceiptsScreen = {}
            )
        }
    }
}