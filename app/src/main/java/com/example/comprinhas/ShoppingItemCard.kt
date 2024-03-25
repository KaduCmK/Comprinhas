package com.example.comprinhas

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.comprinhas.data.TimeDiff

@Composable
fun ShoppingItemCard(
    modifier: Modifier = Modifier,
    id: Long,
    name: String,
    addedBy: String,
    onMoveToCart: () -> Unit,
    onDelete: () -> Unit
) {
    val haptics = LocalHapticFeedback.current
    var isPressed by remember { mutableStateOf(false) }
    val cardColor by animateColorAsState(
        targetValue = if (isPressed) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(300, 50, LinearOutSlowInEasing),
        label = "delete color animation"
    )

    // TODO: hoist do controlador de cor (possivel solucao de bug)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(id) {
                detectTapGestures(
                    onLongPress = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        onDelete()
                    },
                    onPress = {
                        isPressed = true
                        awaitRelease()
                        isPressed = false
                    }
                )
            },
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(25),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
        modifier = modifier.padding(all = 8.dp),
        verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Adicionado por $addedBy | ${TimeDiff.calculateTimeDiff(id)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = onMoveToCart) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_shopping_cart_24),
                    contentDescription = null
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ShoppingItemPreview() {
//    ComprinhasTheme {
//        ShoppingItemCard(
//            id = ZonedDateTime.now().minusHours(4),
//            name = "Compra 1",
//            addedBy = "fulano",
//            onMoveToCart = {},
//            onDelete = {})
//    }
}