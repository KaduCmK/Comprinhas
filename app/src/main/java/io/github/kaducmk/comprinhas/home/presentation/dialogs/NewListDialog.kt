package io.github.kaducmk.comprinhas.home.presentation.dialogs

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
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
import coil.compose.AsyncImage
import io.github.kaducmk.comprinhas.home.data.model.DialogState
import kotlinx.coroutines.delay

@Composable
fun NewListDialog(
    modifier: Modifier = Modifier,
    dialogState: DialogState,
    onDismiss: () -> Unit,
    onSearch: (String) -> Unit,
    onJoinList: (String, String) -> Unit
) {
    var listName by remember { mutableStateOf("") }
    var listPassword by remember { mutableStateOf("") }
    var isValid by remember { mutableStateOf(true) }

    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(
                modifier = modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = if (dialogState.newList) "Criar nova Lista" else "Entrar em uma Lista",
                    style = MaterialTheme.typography.titleMedium
                )
                OutlinedTextField(
                    modifier = Modifier
                        .focusRequester(focusRequester),
                    value = listName,
                    onValueChange = {
                        listName = it
                        if (it.length > 2) onSearch(it)
                    },
                    label = { Text("Nome") },
                    isError = !isValid,
                    supportingText = {
                        if (!isValid) {
                            Text(
                                text = "Nome não pode ser vazio",
                                color = Color.Red
                            )
                        }
                    }
                )
                OutlinedTextField(
                    value = listPassword,
                    onValueChange = { listPassword = it },
                    label = { Text("Senha (opcional)") },
                )
                Spacer(Modifier.height(8.dp))
                if (!dialogState.newList) {
                    Column(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(2.dp)
                            )
                            .height(256.dp)
                            .padding(horizontal = 4.dp)
                            .fillMaxWidth(0.9f)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        when (dialogState) {
                            is DialogState.Loading -> CircularProgressIndicator()
                            is DialogState.Loaded -> {
                                dialogState.searchResult.forEach {
                                    ElevatedCard(modifier = Modifier.padding(4.dp), onClick = {
                                        onJoinList(it.id, listPassword)
                                        onDismiss()
                                    }) {
                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            AsyncImage(
                                                model = it.criador.photoUrl,
                                                contentDescription = "Foto do criador",
                                            )
                                            Column {
                                                Text(
                                                    text = it.nome,
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                                Text(
                                                    text = it.criador.displayName!!,
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text(text = "Cancelar")
                    }
                    if (dialogState.newList)
                        TextButton(
                            onClick = {
                                if (listName.isEmpty()) {
                                    isValid = false

                                } else {
                                    onJoinList(listName, listPassword)
                                    onDismiss()
                                }
                            }
                        ) {
                            Text(text = "${if (dialogState.newList) "Criar" else "Entrar na"} Lista")
                        }
                }
            }
        }

    }

    LaunchedEffect(focusRequester) {
        delay(50)
        focusRequester.requestFocus()
        keyboard?.show()
    }

}

@Preview
@Composable
private fun NewListDialogPreview() {
    NewListDialog(
        onDismiss = {},
        dialogState = DialogState.Loaded(false, emptyList()),
        onSearch = { _ -> },
        onJoinList = { _, _ -> })
}