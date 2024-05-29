package com.example.comprinhas.ui.receipts

data class ReceiptItem(
    val nomeproduto: String = "nomeProduto",
    val quantidade: Int = 123,
    val unidade: String = "UN",
    val valor: Float = 999.9f
)
