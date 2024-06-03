package com.example.comprinhas.http

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

object WorkerOperation {
    const val INSERT = 1
    const val CLEAR_CART = 2
    const val DELETE_FROM_LIST = 3
}

@Deprecated("nao é poggers")
class HttpWorker(context: Context, params: WorkerParameters)
    : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val retrofitService = DatabaseApi.retrofitService

        val listName = inputData.getInt("listName", -1)
        val id = inputData.getLong("id", 0)
        val name = inputData.getString("name") ?: ""
        val addedBy = inputData.getString("addedBy") ?: ""
        val idList = inputData.getLongArray("idList")!!
        val lastChanged = inputData.getLong("lastChanged", 0)

        // TODO: tratamento de erros HTTP

        val operation = inputData.getInt("workerOperation", -1)
        when (operation) {
            WorkerOperation.INSERT -> {
                retrofitService.addNewItem(listName, id, name, addedBy)
            }

            WorkerOperation.DELETE_FROM_LIST -> {
                retrofitService.removeItem(listName, id, addedBy, lastChanged)
            }

            WorkerOperation.CLEAR_CART -> {
                retrofitService.clearCart(idList.toList(), listName, lastChanged)
            }
        }

        return Result.success()
    }
}