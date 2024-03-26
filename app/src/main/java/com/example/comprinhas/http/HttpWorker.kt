package com.example.comprinhas.http

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

object WorkerOperation {
    val INSERT = 1
    val CLEAR_CART = 2
    val DELETE_FROM_LIST = 3

}

class HttpWorker(context: Context, params: WorkerParameters)
    : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }
}