package com.example.comprinhas.core.data.di

import android.content.Context
import android.graphics.Bitmap
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
class QrCodeService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun generateQrCode(data: String): Bitmap {
        val encodedData = Base64.encode(data.toByteArray())

        val qrCodeWriter = QRCodeWriter()
        val bitMatrix = qrCodeWriter.encode(encodedData, BarcodeFormat.QR_CODE, 1024, 1024)
        val bitmap = Bitmap.createBitmap(bitMatrix.width, bitMatrix.height, Bitmap.Config.RGB_565)

        for (x in 0 until bitMatrix.width) {
            for (y in 0 until bitMatrix.height) {
                bitmap.setPixel(
                    x,
                    y,
                    if (bitMatrix.get(x, y)) 0xFF000000.toInt() else 0xFFFFFFFF.toInt()
                )
            }
        }

        return bitmap
    }

    fun scanQrCode(onSuccess: (Barcode) -> Unit, onFailure: (Exception) -> Unit) {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()

        val scanner = GmsBarcodeScanning.getClient(context, options)

        scanner.startScan()
            .addOnSuccessListener(onSuccess)
            .addOnFailureListener(onFailure)
    }
}