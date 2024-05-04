package com.example.comprinhas

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QrCodeScanner
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comprinhas.ui.settings.SettingsActivity
import com.example.comprinhas.ui.theme.ComprinhasTheme

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    showDialog: () -> Unit,
    onNavigateToSettings: () -> Unit = {},
    onQrCodeScan: () -> Unit = {}
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
            val context = LocalContext.current

            Text(
                modifier = modifier.weight(1f),
                text = "Lista de Compras",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight(700)
                )
            )
            IconButton(
                onClick = {
                    context.startActivity(
                        Intent(context,  SettingsActivity::class.java)
                    )
                },
            )  {
                Icon(
                    modifier = Modifier.size(35.dp),
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
        ){
            Button(
                modifier = Modifier.weight(0.3f),
                onClick = showDialog
            ) {
                Text(

                    style = MaterialTheme.typography.titleMedium,
                    text = "Adicionar"
                )
            }
            IconButton(
                modifier = Modifier.padding(start = 64.dp),
                onClick = onQrCodeScan,
            ) {
                Icon(
                    imageVector = Icons.Outlined.QrCodeScanner,
                    contentDescription = "Scan"
                )
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
            TopBar(showDialog = {})
        }
    }
}