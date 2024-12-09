package com.example.comprinhas.list.presentation.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.comprinhas.R
import com.example.comprinhas.list.data.model.CartItem
import com.example.comprinhas.list.data.model.ShoppingItem
import com.example.comprinhas.list.data.model.ShoppingListUiState
import com.example.comprinhas.ui.theme.ComprinhasTheme

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    uiState: ShoppingListUiState,
    onRemoveItem: (CartItem) -> Unit,
    onClearCart: () -> Unit,
) {
    Column(modifier = Modifier.animateContentSize()) {
        Row(
            modifier = modifier.padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
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
            AnimatedContent(
                label = "Cart count animation",
                targetState = uiState.carrinho.size,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInVertically { height -> height } + fadeIn()).togetherWith(
                            slideOutVertically { height -> -height } + fadeOut())
                    }
                    else {
                        (slideInVertically { height -> -height } + fadeIn()).togetherWith(
                            slideOutVertically { height -> height } + fadeOut()
                        )
                    }.using(SizeTransform(clip = false))
                }
            ) {
                Text(
                    text = "$it",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        FilledTonalButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = onClearCart,
            enabled = uiState.carrinho.isNotEmpty()
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
            items(uiState.carrinho) {
                ShoppingItemCard(
                    shoppingItem = ShoppingItem(id = it.id, nome = it.item.nome, adicionadoPor = it.owner),
                    onDelete = {  },
                    onEdit = {  },
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

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
private fun BottomBarPreview() {
    ComprinhasTheme {
        Surface {
            BottomBar(
                uiState = ShoppingListUiState.Loaded(
                    shoppingList = com.example.comprinhas.home.data.model.ShoppingList(
                        id = "1",
                        nome = "Daiso",
                        senha = "",
                        criador = com.example.comprinhas.core.data.model.Usuario(
                            uid = "",
                            displayName = "Lucas",
                            email = "",
                            photoUrl = ""
                        ),
                        participantes = emptyList(),
                        carrinho = emptyList()
                    ),
                    shoppingItems = emptyList(),
                    carrinho = emptyList(),
                    false to null
                ),
                onRemoveItem = {},
                onClearCart = {},
            )
        }
    }
}