package com.example.comprinhas.list.presentation.components

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprinhas.list.data.model.ShoppingItem
import com.example.comprinhas.core.data.TimeDiff
import com.example.comprinhas.core.data.model.Usuario
import com.example.comprinhas.ui.theme.ComprinhasTheme

@Composable
fun ShoppingItemCard(
    modifier: Modifier = Modifier,
    shoppingItem: ShoppingItem,
    onDelete: () -> Unit = {},
    actionButton: @Composable () -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.StartToEnd) {
                onDelete()
                true
            } else false
        }
    )
    val dismissColor by animateColorAsState(
        if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            Color.Transparent
        }, label = "swipe to dismiss"
    )

    SwipeToDismissBox(
        modifier = modifier,
        state = dismissState,
        enableDismissFromEndToStart = false,
        backgroundContent = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(dismissColor)
                    .padding(12.dp, 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(imageVector = Icons.Outlined.Delete, contentDescription = null)
            }
        },
        content = {
            Card(
                modifier = modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(25),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = shoppingItem.nome,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Adicionado por ${shoppingItem.adicionadoPor.displayName}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    actionButton()
                }
            }
        }
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ShoppingItemPreview() {
    ComprinhasTheme {
        ShoppingItemCard(
            shoppingItem = ShoppingItem("0", "Titulo", Usuario()),
            onDelete = {},
            actionButton = {}
        )
    }
}