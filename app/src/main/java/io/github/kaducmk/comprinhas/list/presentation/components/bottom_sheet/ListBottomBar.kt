package io.github.kaducmk.comprinhas.list.presentation.components.bottom_sheet

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.kaducmk.comprinhas.R
import io.github.kaducmk.comprinhas.core.data.model.Usuario
import io.github.kaducmk.comprinhas.home.data.model.ShoppingList
import io.github.kaducmk.comprinhas.list.data.model.ShoppingListUiEvent
import io.github.kaducmk.comprinhas.list.data.model.ShoppingListUiState
import io.github.kaducmk.comprinhas.ui.theme.ComprinhasTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    uiState: ShoppingListUiState,
    scaffoldState: BottomSheetScaffoldState,
    uiEvent: (ShoppingListUiEvent) -> Unit
) {
    val scope = rememberCoroutineScope()

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
                    } else {
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
            onClick = {
                scope.launch { scaffoldState.bottomSheetState.partialExpand() }
                uiEvent(ShoppingListUiEvent.OnClearCart)
            },
            enabled = uiState.carrinho.isNotEmpty()
        ) {
            Icon(
                modifier = Modifier.padding(end = 4.dp),
                painter = painterResource(id = R.drawable.baseline_shopping_cart_checkout_24),
                contentDescription = "Concluir compras"
            )
            Text(text = "Concluir Compras")
        }

        CartList(uiState = uiState, uiEvent = uiEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
private fun BottomBarPreview() {
    ComprinhasTheme {
        Surface {
            BottomBar(
                uiState = ShoppingListUiState.Loaded(
                    currentUser = null,
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
                    carrinho = emptyList(),
                    false to null
                ),
                uiEvent = {},
                scaffoldState = rememberBottomSheetScaffoldState()
            )
        }
    }
}