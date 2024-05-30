package com.example.comprinhas.http

import com.example.comprinhas.ui.receipts.Receipt

data class ResponseBody(val lista: List<Receipt>, val error: String)
