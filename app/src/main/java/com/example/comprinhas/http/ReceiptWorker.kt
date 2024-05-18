package com.example.comprinhas.http

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ReceiptWorker(context: Context, params: WorkerParameters)
    : CoroutineWorker(context, params)
{
    override suspend fun doWork(): Result {
        val retrofitService = DatabaseApi.retrofitService

        val username = inputData.getString("username") ?: ""
        val url = inputData.getString("receipt_url") ?: ""

        Log.d("ReceiptWorker", "username: $username, url: $url")

        val response = retrofitService.newReceipt(BodyRequest(username, url))

        if (response.code() == 200) {
            Toast.makeText(applicationContext, "Recibo Adicionado", Toast.LENGTH_SHORT).show()
            return Result.success()
        }
        else {
            Log.e("ReceiptWorker", "Error: ${response.body().toString()}")
            return Result.failure()
        }
    }
}