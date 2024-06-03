package com.example.comprinhas

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
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprinhas.data.shoppingItem.ShoppingItem
import com.example.comprinhas.data.TimeDiff
import com.example.comprinhas.ui.theme.ComprinhasTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingItemCard(
    modifier: Modifier = Modifier,
    shoppingItem: ShoppingItem,
    onDelete: () -> Unit = {},
    actionButton: @Composable () -> Unit,
) {
    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (it == DismissValue.DismissedToEnd) {
                onDelete()
                true
            } else false
        },
        positionalThreshold = { 150.dp.toPx()}
    )
    val dismissColor by animateColorAsState(
        if (dismissState.dismissDirection == DismissDirection.StartToEnd) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            Color.Transparent
        }, label = "swipe to dismiss"
    )

    SwipeToDismiss(
        modifier = modifier,
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd),
        background = {
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
        dismissContent = {
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
                            text = shoppingItem.nomeItem,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Adicionado por ${shoppingItem.adicionadoPor} | ${
                                TimeDiff.calculateTimeDiff(
                                    shoppingItem.idItem
                                )
                            }",
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
            shoppingItem = ShoppingItem(0, "Titulo", "Fulano"),
            onDelete = {},
            actionButton = {}
        )
    }
}