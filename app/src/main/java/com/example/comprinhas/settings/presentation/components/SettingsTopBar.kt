package com.example.comprinhas.settings.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comprinhas.ui.theme.ComprinhasTheme

@Composable
fun SettingsTopBar(
    onBackPressed: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackPressed
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Default.ArrowBack, contentDescription = "Back Button")
        }
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = "Configurações",
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight(700)
            )
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsTopBarPreview() {
    ComprinhasTheme {
        Surface {
            SettingsTopBar {}
        }
    }
}