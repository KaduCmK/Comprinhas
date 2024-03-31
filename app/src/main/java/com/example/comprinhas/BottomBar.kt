package com.example.comprinhas

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comprinhas.data.ShoppingItem
import com.example.comprinhas.ui.theme.ComprinhasTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    cartFlow: Flow<List<ShoppingItem>>,
    isExpanded: Boolean,
    toggleExpanded: () -> Unit,
    onRemoveItem: (ShoppingItem) -> Unit,
    onClearCart: () -> Unit
) {
    val surfaceColor by animateColorAsState(
        if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        label = "expanded color animation"
    )
    val cartItems by cartFlow.collectAsState(initial = List(3) { ShoppingItem(0, "Compra $it") })

    Column {
        Surface(
            modifier = modifier
                .animateContentSize()
                .fillMaxWidth()
                .heightIn(min = 72.dp),
            shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
            onClick = toggleExpanded,
            color = surfaceColor,
            shadowElevation = 32.dp
        ) {
            Column {
                Row(
                    modifier = modifier.padding(all = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = modifier
                            .size(31.dp)
                            .weight(1f),
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(5f),
                        text = "Carrinho",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight(700)
                        )
                    )
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = null
                    )
                }

                if (isExpanded) {
                    FilledTonalButton(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = onClearCart
                    ) {
                        Icon(
                            modifier = Modifier.padding(end = 4.dp),
                            painter = painterResource(id = R.drawable.baseline_shopping_cart_checkout_24),
                            contentDescription = "Concluir compras"
                        )
                        Text(text = "Concluir Compras")
                    }

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(cartItems) {
                            ShoppingItemCard(
                                defaultColor = MaterialTheme.colorScheme.surfaceTint,
                                shoppingItem = it,
                                actionButton = {
                                    IconButton(onClick = { onRemoveItem(it) }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_remove_shopping_cart_24),
                                            contentDescription = null
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
private fun BottomBarPreview() {
    ComprinhasTheme {
        Surface {
            BottomBar(
                isExpanded = true,
                toggleExpanded = {},
                onRemoveItem = {},
                onClearCart = {},
                cartFlow = flowOf(List(3) { ShoppingItem(0, "Compra $it") })
            )
        }
    }
}