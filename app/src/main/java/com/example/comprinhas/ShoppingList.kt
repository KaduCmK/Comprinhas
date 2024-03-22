package com.example.comprinhas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ShoppingList(
    modifier: Modifier = Modifier,
    shoppingList: List<ShoppingItem> = List(3) { ShoppingItem("Compra $it", "Fulano")},
    isExpanded: Boolean,
    onMoveToCart: (ShoppingItem) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .blur(if (isExpanded) 16.dp else 0.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(shoppingList) {
            ShoppingItemCard(name = it.name, addedBy = it.addedBy, onMoveToCart = { onMoveToCart(it) })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShoppingListPreview() {
    ShoppingList(isExpanded = false, onMoveToCart = {})
}