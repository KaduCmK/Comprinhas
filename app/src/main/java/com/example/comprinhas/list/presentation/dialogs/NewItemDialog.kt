package com.example.comprinhas.list.presentation.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.comprinhas.list.data.model.ShoppingItem
import kotlinx.coroutines.delay

@Composable
fun NewItemDialog(
    editItem: ShoppingItem?,
    onDismiss: () -> Unit,
    onConfirm: (String, String?) -> Unit
) {
    var txtField by remember {
        mutableStateOf(
            TextFieldValue(
            text = editItem?.nome ?: "",
            selection = TextRange(editItem?.nome?.length ?: 0)
        ))
    }

    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    LaunchedEffect(focusRequester) {
        delay(50)
        focusRequester.requestFocus()
        keyboard?.show()
    }

    Dialog(onDismissRequest = onDismiss) {
        Card {
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                text = "${if (editItem == null) "Adicionar" else "Editar"} Item",
                style = MaterialTheme.typography.titleMedium
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .align(Alignment.CenterHorizontally)
                    .focusRequester(focusRequester),
                value = txtField,
                onValueChange = { txtField = it },
                label = { Text("Nome") },
                singleLine = true
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(text = "Cancelar")
                }
                TextButton(
                    onClick = { onConfirm(txtField.text, editItem?.id) },
                    enabled = txtField.text.isNotEmpty()
                ) {
                    Text(text = "Confirmar")
                }
            }
        }
    }
}

@Preview
@Composable
private fun NewItemDialogPreview() {
    NewItemDialog(null, {}, { _, _ -> })
}