package com.example.comprinhas.home.presentation.dialogs

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.comprinhas.ui.theme.ComprinhasTheme

@Composable
fun EditListDialog(
    onDismissRequest: () -> Unit,
    listName: String,
    onDeleteList: () -> Unit,
    onEditList: (String) -> Unit = {},
    onExitList: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 32.dp, end = 32.dp, top = 8.dp)
                    .align(Alignment.CenterHorizontally),
                text = listName,
                style = MaterialTheme.typography.titleLarge
            )

            TextButton(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                onClick = onDeleteList
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = "Delete List"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Apagar Lista",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
            TextButton(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                onClick = onExitList
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = "Exit List"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Sair da Lista",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EditListDialogPreview() {
    ComprinhasTheme {
        EditListDialog({}, "Daiso", {}, {}, {})
    }
}