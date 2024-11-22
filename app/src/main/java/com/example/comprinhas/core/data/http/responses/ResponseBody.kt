package com.example.comprinhas.core.data.http.responses

import com.example.comprinhas.receipts_list.data.model.Receipt

data class ResponseBody(val lista: List<Receipt>, val error: String)
