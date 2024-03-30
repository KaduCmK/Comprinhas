package com.example.comprinhas.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprinhas.ui.theme.ComprinhasTheme

@Composable
fun SettingTextField(
    modifier: Modifier = Modifier,
    label: String,
    fieldWidth: Int = 32,
    fieldValue: String,
    onChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(text = label)
        TextField(
            modifier = Modifier
                .padding(horizontal = fieldWidth.dp),
            value = fieldValue,
            onValueChange = onChange)
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingTextFieldPreview() {
    ComprinhasTheme {
        SettingTextField(label = "Label", fieldValue = "") {}
    }
}