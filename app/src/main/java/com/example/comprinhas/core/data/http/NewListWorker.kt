package com.example.comprinhas.core.data.http

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class NewListWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext,
    params
) {
    override suspend fun doWork(): Result {
        val retrofitService = DatabaseApi.retrofitService

        val username = inputData.getString("username") ?: ""
        val listName = inputData.getString("listName") ?: ""
        val listPassword = inputData.getString("listPassword") ?: ""

        retrofitService.createList(username, listName, listPassword)

        return Result.success()
    }
}