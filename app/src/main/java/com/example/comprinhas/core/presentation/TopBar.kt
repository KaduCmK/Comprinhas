package com.example.comprinhas.core.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Settings
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
import com.example.comprinhas.home.data.model.HomeUiState
import com.example.comprinhas.ui.UiState
import com.example.comprinhas.ui.theme.ComprinhasTheme

@Composable
fun TopBar(
    title: String,
    subtitle: String? = null,
    backButton: (() -> Unit)? = null,
    topButton: @Composable (() -> Unit)? = null,
    bottomButton: @Composable (() -> Unit)? = null,
    mainButton: @Composable ((uiState: HomeUiState) -> Unit)? = null,
    uiState: HomeUiState,
) {
    Surface {
        Column(
            modifier = Modifier.padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (backButton != null) {
                    IconButton(
                        onClick = backButton
                    ) {
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back Button"
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight(700)
                        )
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodyMedium
                                .copy(fontStyle = FontStyle.Italic)
                        )
                    }
                }
                if (topButton != null) topButton()
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (mainButton != null) mainButton(uiState)

                if (bottomButton != null) bottomButton()
            }

            if (uiState is HomeUiState.NoInternet) {
                Row(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        imageVector = Icons.Outlined.CloudOff,
                        contentDescription = "No Connection"
                    )
                    Text(
                        text = "Sem conexão com o servidor",
                        style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic)
                    )
                }
            }
            else if (uiState is HomeUiState.Loading) {
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator()
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeTopBarPreview() {
    ComprinhasTheme {
        TopBar(
            title = "Lista de Compras",
            subtitle = "Mojo Dojo",
            topButton = {
                IconButton(onClick = {}) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = null
                    )
                }
            },
            mainButton = {
                Button(
                    enabled = it is HomeUiState.Loaded,
                    onClick = {}
                ) {
                    Text(

                        style = MaterialTheme.typography.titleMedium,
                        text = "Adicionar"
                    )
                }
            },
            bottomButton = {
                IconButton(
                    onClick = {},
                )  {
                    Icon(imageVector = Icons.AutoMirrored.Outlined.ReceiptLong, contentDescription = null)
                }
            },
            uiState = HomeUiState.Loading(null)
        )
    }
}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//private fun SettingsTopBarPreview() {
//    ComprinhasTheme {
//        TopBar(
//            title = "Configurações",
//            backButton = {},
//            uiState = HomeUiState.Loading(null)
//        )
//    }
//}
//
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//private fun ReceiptsTopBarPreview() {
//    ComprinhasTheme {
//        TopBar(
//            title = "Notas Fiscais",
//            backButton = {},
//            mainButton = {
//                Button(onClick = {}) {
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Icon(
//                            imageVector = Icons.Outlined.QrCodeScanner,
//                            contentDescription = "Escanear QR Code"
//                        )
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text(text = "Escanear QR Code")
//                    }
//                }
//            },
//            uiState = HomeUiState.Loading(null)
//        )
//    }
//}