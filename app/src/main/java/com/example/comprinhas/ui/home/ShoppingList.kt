package com.example.comprinhas.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprinhas.R
import com.example.comprinhas.ShoppingItemCard
import com.example.comprinhas.data.ShoppingItem
import com.example.comprinhas.ui.theme.ComprinhasTheme

@Composable
fun ShoppingList(
    modifier: Modifier = Modifier,
    shoppingList: List<ShoppingItem>,
    onMoveToCart: (ShoppingItem) -> Unit,
    onDelete: (ShoppingItem) -> Unit,
    isLoading: Boolean
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        if (isLoading) {
            item {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(32.dp)
                    )
                }
            }
        }
        else {
            items(shoppingList) {
                ShoppingItemCard(
                    shoppingItem = it,
                    onLongPress = { onDelete(it) },
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

@Preview(showBackground = true)
@Composable
private fun ShoppingListPreview() {
    ComprinhasTheme {
        ShoppingList(
            shoppingList = List(4) {ShoppingItem(name = "Compa $it")},
            onMoveToCart = {},
            onDelete = {},
            isLoading = false
        )
    }
}