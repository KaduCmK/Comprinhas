package com.example.comprinhas.http

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.comprinhas.data.ShoppingListDatabase
import com.example.comprinhas.data.ShoppingListRepository
import kotlinx.coroutines.flow.first

class SyncWorker(private val context: Context, params: WorkerParameters)
    : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val localLastChanged = inputData.getLong("localLastChanged", -1)
        val onlineDatabase = DatabaseApi.retrofitService.getDatabase()
        val dao = ShoppingListDatabase.getDatabase(context).shoppingItemDao()
        val localRepo = ShoppingListRepository(dao, context)

        if (onlineDatabase.lastChanged != localLastChanged) {
            val works = localRepo.pendingWorks

            val workManager = WorkManager.getInstance(context)

            if (works.size > 0) {
                workManager.enqueueUniqueWork(
                    "taskSync",
                    ExistingWorkPolicy.REPLACE,
                    works)
            }

            workManager.getWorkInfosForUniqueWorkFlow("taskSync")
                .collect  { list ->
                    if (list.all { it.state == WorkInfo.State.SUCCEEDED }) {
                        Log.d("SYNC", "Tasks finalizadas. Sincronizando...")

                        localRepo.clearList()
                        onlineDatabase.shoppingList.forEach { dao.insert(it) }
                    }
                }
        }

        return Result.success()
    }
}