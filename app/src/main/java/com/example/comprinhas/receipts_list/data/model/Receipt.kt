package com.example.comprinhas.receipts_list.data.model

data class Receipt(
    val chaveAcesso: String = "00000000000000000000000000000000000000000000",
    val dataEmissao: String =  "2020-01-01T00:00:00.000Z",
    val nomeMercado: String = "SUPERMERCADO PIPIPI POPOPO",
    val cnpjMercado: String = "00000000000000",
    val enderecoMercado: String = "RUA SAO FRANCISCO XAVIER, 691 - SAO FRANCISCO XAVIER RIO DE JANEIRO",
    val valorTotal: Float = 98.90f,
    val itens: List<ReceiptItem> = emptyList()
)
