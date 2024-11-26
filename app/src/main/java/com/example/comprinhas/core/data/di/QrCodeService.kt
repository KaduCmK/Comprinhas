package com.example.comprinhas.core.data.di

import android.content.Context
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class QrCodeService @Inject constructor(
    @ApplicationContext private val context: Context
) {
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