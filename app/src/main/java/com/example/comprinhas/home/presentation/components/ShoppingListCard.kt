package com.example.comprinhas.home.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.comprinhas.R
import com.example.comprinhas.core.data.model.Usuario
import com.example.comprinhas.home.data.model.ShoppingList
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
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(shoppingList.imgUrl)
            .size(Size.ORIGINAL)
            .crossfade(true)
            .build()
    )

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onCardClick,
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onCardHold(shoppingList)
                }
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(164.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = CardDefaults.elevatedShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(painter, contentDescription = null, contentScale = ContentScale.FillWidth)
            if (painter.state !is AsyncImagePainter.State.Success)
                Icon(
                    modifier = Modifier.size(64.dp),
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null
                )
        }
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = shoppingList.nome,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "4 itens",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            HorizontalDivider()
            Row(
                modifier = Modifier.padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy((-12).dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    modifier = Modifier.padding(end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = R.drawable.pictogrammers_material_crown),
                        contentDescription = null
                    )
                    Box(
                        modifier = modifier
                            .size(32.dp)
                            .shadow(8.dp, shape = CircleShape)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                    ) {
                        AsyncImage(
                            modifier = Modifier.fillMaxSize(),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(shoppingList.criador.photoUrl)
                                .crossfade(true)
                                .build(),
                            placeholder = rememberVectorPainter(Icons.Default.Person),
                            contentDescription = null
                        )
                    }
                }
                shoppingList.participantes.forEach { _ ->
                    Box(
                        modifier = modifier
                            .size(28.dp)
                            .shadow(8.dp, shape = CircleShape)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                    ) {
                        AsyncImage(
                            modifier = Modifier.fillMaxSize(),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("it.imgUrl")
                                .crossfade(true)
                                .build(),
                            placeholder = rememberVectorPainter(Icons.Default.Person),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ShoppingListCardPreview() {
    val creator = Usuario("1", "Kadu", "")
    ComprinhasTheme {
        ShoppingListCard(
            shoppingList = ShoppingList(
                id = "-1",
                nome = "Daiso",
                senha = "123",
                criador = creator,
                participantes = emptyList()
            ),
            onCardClick = {},
            onCardHold = {}
        )
    }
}