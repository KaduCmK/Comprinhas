package com.example.comprinhas.ui.list

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprinhas.R
import com.example.comprinhas.ShoppingItemCard
import com.example.comprinhas.data.shoppingItem.ShoppingItem
import com.example.comprinhas.ui.theme.ComprinhasTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShoppingList(
    modifier: Modifier = Modifier,
    shoppingList: List<ShoppingItem>,
    onMoveToCart: (ShoppingItem) -> Unit,
    onDelete: (ShoppingItem) -> Unit,
) {

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
        }
        else {
            items(items = shoppingList, key = { it.idItem }) {
                ShoppingItemCard(
                    modifier = Modifier.animateItemPlacement(),
                    shoppingItem = it,
                    onDelete = { onDelete(it) },
                    actionButton = {
                        IconButton(onClick = { onMoveToCart(it) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_add_shopping_cart_24),
                                contentDescription = "adicionar ao carrinho")
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
                onMoveToCart = {},
                onDelete = {},
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
                shoppingList = List(4) { ShoppingItem(idItem = it.toLong(), nomeItem = "Compra $it") },
                onMoveToCart = {},
                onDelete = {},
            )
        }
    }
}