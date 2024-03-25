package com.example.comprinhas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import com.example.comprinhas.data.ShoppingItem
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShoppingList(
    modifier: Modifier = Modifier,
    shoppingFlow: Flow<List<ShoppingItem>>,
    isExpanded: Boolean,
    onMoveToCart: (ShoppingItem) -> Unit,
    onDelete: (ShoppingItem) -> Unit
) {
    val shoppingList by shoppingFlow.collectAsState(initial = emptyList())
    
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .blur(if (isExpanded) 16.dp else 0.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(shoppingList) {
            ShoppingItemCard(
                id =  it.id,
                name = it.name,
                addedBy = it.addedBy,
                onMoveToCart = { onMoveToCart(it) },
                onDelete = { onDelete(it) }
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun ShoppingListPreview() {
//    ShoppingList(shoppingList = List(3) { ShoppingItem(name = it.toString(), addedBy = "Preview") }, isExpanded = false, onMoveToCart = {}, onDelete = {})
//}