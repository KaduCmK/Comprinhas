package com.example.comprinhas

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay

@Composable
fun InputDialog(
    onDismiss: () -> Unit,
    setValue: (String) -> Unit
) {

    var txtField by remember { mutableStateOf("") }
    var isValid by remember { mutableStateOf(true) }

    val showKeyboard by remember { mutableStateOf(true) }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                text = "Adicionar Item",
                style = MaterialTheme.typography.titleMedium
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(8.dp)
                    .focusRequester(focusRequester),
                value = txtField,
                onValueChange = { txtField = it},
                label = { Text("Item")},
                isError = !isValid,
                supportingText = {
                    if (!isValid) {
                        Text(
                            text = "Não pode ser vazio",
                            color = Color.Red
                        )
                    }
                }
            )
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(text = "Cancelar")
                }
                TextButton(
                    onClick = {
                        if (txtField.isEmpty()) {
                            isValid = false

                        } else {
                        setValue(txtField)
                        onDismiss()
                        }
                    }
                ) {
                    Text(text = "Adicionar")
                }
            }
        }

    }

    LaunchedEffect(focusRequester) {
        if (showKeyboard) {
            delay(50)
            focusRequester.requestFocus()
            keyboard?.show()
        }
    }
}

@Preview
@Composable
private fun InputDialogPreview() {
    InputDialog({}, {})
}