package com.example.comprinhas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprinhas.data.ShoppingItem
import com.example.comprinhas.ui.theme.ComprinhasTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

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
            .fillMaxSize()
            .blur(if (isExpanded) 16.dp else 0.dp)
            .padding(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
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

@Preview(showBackground = true)
@Composable
private fun ShoppingListPreview() {
    ComprinhasTheme {
        ShoppingList(
            shoppingFlow = flowOf(List(3) { ShoppingItem(0, "Compra $it") }),
            isExpanded = false,
            onMoveToCart = {}
        ) {
            
        }
    }
}