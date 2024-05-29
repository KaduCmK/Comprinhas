package com.example.comprinhas.http

import com.example.comprinhas.ui.receipts.Receipt

data class ReceiptResponse(val lista: List<Receipt>, val error: String)
