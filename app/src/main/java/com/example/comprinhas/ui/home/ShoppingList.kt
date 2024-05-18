package com.example.comprinhas.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprinhas.R
import com.example.comprinhas.ShoppingItemCard
import com.example.comprinhas.data.ShoppingItem
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

@Preview(showBackground = true)
@Composable
private fun ShoppingListPreview() {
    ComprinhasTheme {
        ShoppingList(
            shoppingList = List(4) {ShoppingItem(idItem = it.toLong(), nomeItem = "Compra $it")},
            onMoveToCart = {},
            onDelete = {},
        )
    }
}