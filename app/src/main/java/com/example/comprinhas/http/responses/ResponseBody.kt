package com.example.comprinhas.http.responses

import com.example.comprinhas.ui.receipts.Receipt

data class ResponseBody(val lista: List<Receipt>, val error: String)
