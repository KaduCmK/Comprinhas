package com.example.comprinhas.ui.receipts

data class Receipt(
    val chaveAcesso: String,
    val dataEmissao: String,
    val nomeMercado: String,
    val valorTotal: Float,
    val itens: List<ReceiptItem>
)
