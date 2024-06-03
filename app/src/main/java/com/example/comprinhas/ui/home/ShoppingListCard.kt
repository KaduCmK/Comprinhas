package com.example.comprinhas.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprinhas.data.shoppingList.ShoppingList
import com.example.comprinhas.ui.theme.ComprinhasTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShoppingListCard(
    modifier: Modifier = Modifier,
    shoppingList: ShoppingList,
    onCardClick: () -> Unit,
    onCardHold: (ShoppingList) -> Unit
) {
    val haptics = LocalHapticFeedback.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onCardClick,
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onCardHold(shoppingList)
                }
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceTint)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = shoppingList.nomeLista,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Text(
                text = "4 itens",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Criado por ${shoppingList.criadoPor}",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ShoppingListCardPreview() {
    ComprinhasTheme {
        ShoppingListCard(
            shoppingList = ShoppingList(
                idLista = -1,
                nomeLista = "Daiso",
                senhaLista = "123",
                criadoPor = "Kadu"
            ),
            onCardClick = {},
            onCardHold = {}
        )
    }
}