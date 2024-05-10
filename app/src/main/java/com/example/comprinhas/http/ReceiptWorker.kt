package com.example.comprinhas.http

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ReceiptWorker(context: Context, params: WorkerParameters)
    : CoroutineWorker(context, params)
{
    override suspend fun doWork(): Result {
        val retrofitService = DatabaseApi.retrofitService

        val url = inputData.getString("receipt_url") ?: ""
        Log.d("ReceiptWorker", "url: $url")

        retrofitService.newReceipt(BodyRequest(url))

        return Result.success()
    }
}