package io.github.kaducmk.comprinhas.list.presentation.components.bottom_sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kaducmk.comprinhas.R
import io.github.kaducmk.comprinhas.core.data.model.Usuario
import io.github.kaducmk.comprinhas.home.data.model.ShoppingList
import io.github.kaducmk.comprinhas.list.data.model.CartItem
import io.github.kaducmk.comprinhas.list.data.model.ShoppingItem
import io.github.kaducmk.comprinhas.list.data.model.ShoppingListUiEvent
import io.github.kaducmk.comprinhas.list.data.model.ShoppingListUiState
import io.github.kaducmk.comprinhas.list.presentation.components.ShoppingItemCard
import io.github.kaducmk.comprinhas.ui.theme.ComprinhasTheme

@Composable
fun CartList(
    modifier: Modifier = Modifier,
    uiState: ShoppingListUiState,
    uiEvent: (ShoppingListUiEvent) -> Unit
) {
    val groupedItems = uiState.carrinho
        .filter { it.owner.uid != uiState.currentUser?.uid }
        .groupBy { it.owner.displayName }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(uiState.carrinho.filter { it.owner.uid == uiState.currentUser?.uid }) {
            ShoppingItemCard(
                shoppingItem = it.item,
                actionButton = {
                    IconButton(onClick = { uiEvent(ShoppingListUiEvent.OnRemoveFromCart(it)) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_remove_shopping_cart_24),
                            contentDescription = null
                        )
                    }
                }
            )
        }

        groupedItems.forEach { (displayName, items) ->
            item {
                Row(
                    modifier = Modifier.fillMaxSize().padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null
                    )
                    Text(
                        text = "Carrinho de $displayName",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            items(items) { ShoppingItemCard(shoppingItem = it.item, actionButton = {}) }
        }
    }
}

@Preview
@Composable
private fun CartListPreview() {
    ComprinhasTheme {
        Surface {
            CartList(uiState = ShoppingListUiState.Loaded(
                currentUser = Usuario(
                    uid = "",
                    displayName = "Lucas",
                    email = "",
                    photoUrl = ""
                ),
                shoppingList = ShoppingList(
                    id = "1",
                    nome = "Daiso",
                    senha = "",
                    criador = Usuario(
                        uid = "",
                        displayName = "Lucas",
                        email = "",
                        photoUrl = ""
                    ),
                    participantes = emptyList(),
                    carrinho = emptyList()
                ),
                shoppingItems = emptyList(),
                carrinho = listOf(
                    CartItem(
                        id = "",
                        item = ShoppingItem(
                            id = "1",
                            nome = "Arroz",
                            adicionadoPor = Usuario(
                                uid = "",
                                displayName = "Lucas",
                                email = "",
                                photoUrl = ""
                            )
                        ),
                        owner = Usuario(
                            uid = "",
                            displayName = "Lucas",
                            email = "",
                            photoUrl = ""
                        ),
                    ),
                    CartItem(
                        id = "1",
                        item = ShoppingItem(
                            id = "2",
                            nome = "Arroz",
                            adicionadoPor = Usuario(
                                uid = "",
                                displayName = "Lucas",
                                email = "",
                                photoUrl = ""
                            )
                        ),
                        owner = Usuario(
                            uid = "3",
                            displayName = "Lucas",
                            email = "",
                            photoUrl = ""
                        ),
                    )
                ),
                false to null
            ),
                uiEvent = {})
        }
    }
}