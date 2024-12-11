package io.github.kaducmk.comprinhas.list.presentation.dialogs

import android.app.Activity
import android.graphics.Bitmap
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.kaducmk.comprinhas.ui.theme.ComprinhasTheme

@Composable
fun QrCodeDialog(modifier: Modifier = Modifier, bitmap: Bitmap, onDismiss: () -> Unit) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context as Activity
        val layoutParams = activity.window.attributes
        layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL
        activity.window.attributes = layoutParams
        onDispose {
            layoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
            activity.window.attributes = layoutParams
        }
    }

    Dialog(onDismiss) {
        Card {
            Column(
                modifier = modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Compartilhar Lista",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Image(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)),
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "QRCode"
                )
                Text(
                    "Escaneie o QRCode no dispositivo que deseja acessar esta lista",
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onDismiss) {
                    Text("Fechar")
                }
            }
        }
    }
}

@Preview
@Composable
private fun QrCodeDialogPreview() {
    ComprinhasTheme {
        QrCodeDialog(
            onDismiss = {},
            bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888)
        )
    }
}