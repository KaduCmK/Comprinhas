package com.example.comprinhas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    showDialog: () -> Unit
) {
    Column(
        modifier = modifier.blur(if (isExpanded) 16.dp else 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = modifier.weight(1f),
                text = "Lista de Compras",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight(700)
                )
            )
            Icon(
                imageVector = Icons.Outlined.Settings,
                modifier = Modifier.size(35.dp),
                contentDescription = null
            )
        }

        Button(
            onClick = showDialog
        ) {
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = "Adicionar"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopBarPreview() {
    TopBar(isExpanded = false, showDialog = {})
}