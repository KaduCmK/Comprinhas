package com.example.comprinhas.list.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprinhas.R
import com.example.comprinhas.core.data.model.Usuario
import com.example.comprinhas.list.data.model.ShoppingItem
import com.example.comprinhas.list.data.model.ShoppingListUiEvent
import com.example.comprinhas.ui.theme.ComprinhasTheme

@Composable
fun ShoppingList(
    modifier: Modifier = Modifier,
    shoppingList: List<ShoppingItem>,
    uiEvent: (ShoppingListUiEvent) -> Unit
) {
    var expandedCardUid by remember {
        mutableStateOf("")
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        if (shoppingList.isEmpty()) {
            item {
                Column(
                    modifier = Modifier.fillParentMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        imageVector = Icons.Outlined.ShoppingCart,
                        tint = MaterialTheme.colorScheme.surfaceVariant,
                        contentDescription = "Empty Cart"
                    )
                    Text(
                        text = "Sua Lista está vazia",
                        style = MaterialTheme.typography.titleMedium.copy(fontStyle = FontStyle.Italic),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        } else {
            items(items = shoppingList, key = { it.id!! }) {
                ShoppingItemCard(
                    modifier = Modifier.animateItem(),
                    shoppingItem = it,
                    expanded = expandedCardUid == it.id,
                    onClick = {
                        expandedCardUid = if (expandedCardUid == it.id) "" else it.id!!
                    },
                    onDelete = { uiEvent(ShoppingListUiEvent.OnDeleteShoppingItem(it.id!!)) },
                    onEdit = { uiEvent(ShoppingListUiEvent.OnToggleDialog(true, it)) },
                    actionButton = {
                        IconButton(onClick = { uiEvent(ShoppingListUiEvent.OnAddToCart(it)) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_add_shopping_cart_24),
                                contentDescription = "adicionar ao carrinho"
                            )
                        }
                    }
                )
            }
        }

    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EmptyPreview() {
    ComprinhasTheme {
        Surface {
            ShoppingList(
                shoppingList = emptyList(),
                uiEvent = {}
            )
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    ComprinhasTheme {
        Surface {
            ShoppingList(
                shoppingList = List(4) {
                    ShoppingItem(
                        id = it.toString(),
                        nome = "Compra $it",
                        adicionadoPor = Usuario()
                    )
                },
                uiEvent = {}
            )
        }
    }
}