package com.example.comprinhas.list.presentation.components

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.comprinhas.core.data.model.Usuario
import com.example.comprinhas.list.data.model.ShoppingItem
import com.example.comprinhas.ui.theme.ComprinhasTheme

@Composable
fun ShoppingItemCard(
    modifier: Modifier = Modifier,
    shoppingItem: ShoppingItem,
    expanded: Boolean = false,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onClick: () -> Unit = {},
    actionButton: @Composable () -> Unit,
) {
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = modifier.animateContentSize(),
        verticalArrangement = Arrangement.spacedBy((-12).dp)
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(0.2f),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            }
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
        if (expanded) {
            OutlinedCard(
                shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = onEdit) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ShoppingItemPreview() {
    ComprinhasTheme {
        ShoppingItemCard(
            shoppingItem = ShoppingItem("0", "Titulo", Usuario()),
            expanded = true,
            onDelete = {},
            onEdit = {},
            onClick = {},
            actionButton = {}
        )
    }
}